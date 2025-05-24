package com.java.EcomerceApp.service.cart;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;

    public CartServiceImpl (ProductRepository productRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, ModelMapper modelMapper,  AuthUtil authUtil){
        this.productRepository = productRepository;
        this.authUtil = authUtil;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        // Retrieve the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        // Check product availability/quantity
        if (product.getQuantity() < quantity) {
            throw new ResourceNotFoundException("Product not available in the required quantity");
        }
        // Retrieve or create the cart if it does not exist
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
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity()); // Update product quantity
        cart.getCartItems().add(newCartItem);
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        return getCartDTO(cart);
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

    @Override
    public CartDTO getCart() {
        Cart cart = cartRepository.findCartByUserEmail(authUtil.loggedInUserEmail());
        if (cart == null) {
            throw new ResourceNotFoundException("Cart is empty");
        }
        return getCartDTO(cart);
    }

    private CartDTO getCartDTO(Cart cart) {
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        List<ProductDTO> productDTOList = cartItems.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            productDTO.setDiscount(item.getDiscount());
            productDTO.setPrice(item.getProductPrice());
            return productDTO;
        }).collect(Collectors.toList());
        cartDTO.setProducts(productDTOList);
        return cartDTO;
    }

    @Override
    public CartDTO removeProductFromCart(Long productId) {
        Cart cart = cartRepository.findCartByUserEmail(authUtil.loggedInUserEmail());
        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found");
        }

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getCartId(), productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("Product not found in cart");
        }

        cart.getCartItems().remove(cartItem);
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartItemRepository.delete(cartItem);
        cartRepository.save(cart);

        return getCartDTO(cart);
    }

    @Override
    public CartDTO updateProductQuantity(Long productId, Integer quantity) {
        if (quantity <= 0) {
            throw new APIException("Quantity must be greater than zero");
        }

        Cart cart = cartRepository.findCartByUserEmail(authUtil.loggedInUserEmail());
        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found");
        }

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getCartId(), productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("Product not found in cart");
        }

        Product product = cartItem.getProduct();
        if (product.getQuantity() < quantity) {
            throw new APIException("Product " + product.getProductName() + " is not available in the required quantity");
        }

        // Adjust the cart total based on the quantity change
        double oldTotal = cartItem.getProductPrice() * cartItem.getQuantity();
        double newTotal = cartItem.getProductPrice() * quantity;
        cart.setTotalPrice(cart.getTotalPrice() - oldTotal + newTotal);

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

        return getCartDTO(cart);
    }

    @Override
    @Transactional
    public void clearCart() {
        Cart cart = cartRepository.findCartByUserEmail(authUtil.loggedInUserEmail());
        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found");
        }
        
        clearCart(cart);
    }

    @Transactional
    public void clearCart(Cart cart) {
        cart.clearCartItems();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }
}
