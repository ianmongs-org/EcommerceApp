package com.java.EcomerceApp.service.cart;

import java.util.List;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.EcomerceApp.dto.CartDTO;
import com.java.EcomerceApp.dto.ProductDTO;
import com.java.EcomerceApp.exception.APIException;
import com.java.EcomerceApp.exception.ResourceNotFoundException;
import com.java.EcomerceApp.model.Cart;
import com.java.EcomerceApp.model.CartItem;
import com.java.EcomerceApp.model.Product;
import com.java.EcomerceApp.repository.CartItemRepository;
import com.java.EcomerceApp.repository.CartRepository;
import com.java.EcomerceApp.repository.ProductRepository;
import com.java.EcomerceApp.security.utils.AuthUtil;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //retrieve the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        //product availability/quantity
        if (product.getQuantity() < quantity) {
            throw new ResourceNotFoundException("Product not available in the required quantity");
        }
        //retrieve or create the cart if it does not exist
        Cart cart = createCart();
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getCartId(), product.getProductId());
        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }
        if (product.getQuantity() == 0) {
            throw new APIException("Product " + product.getProductName() + " is out of stock");
        }
        if (product.getQuantity() < quantity) {
            throw new APIException("Product " + product.getProductName() + " is not available in the required quantity");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getPrice());

        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(newCartItem.getProduct(), ProductDTO.class);
            productDTO.setQuantity(newCartItem.getQuantity());
            productDTO.setDiscount(newCartItem.getDiscount());
            productDTO.setPrice(newCartItem.getProductPrice());
            return productDTO;
        });

        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByUserEmail(authUtil.loggedInUserEmail());
        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setUser(authUtil.loggedInUser());
        cart.setTotalPrice(0.0);
        return cartRepository.save(cart);
    }
}
