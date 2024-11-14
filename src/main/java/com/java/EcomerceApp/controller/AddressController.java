package com.java.EcomerceApp.controller;

import com.java.EcomerceApp.dto.AddressDTO;
import com.java.EcomerceApp.model.User;
import com.java.EcomerceApp.security.utils.AuthUtil;
import com.java.EcomerceApp.service.address.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AuthUtil authUtil;
    private final AddressService addressService;

    public AddressController(AuthUtil authUtil, AddressService addressService) {
        this.authUtil = authUtil;
        this.addressService = addressService;
    }

    @PostMapping("/address")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        List<AddressDTO> addressDTOList = addressService.getAddress();
        return new ResponseEntity<>(addressDTOList, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressDTOs = addressService.getUserAddresses(user);
        return new ResponseEntity<>(addressDTOs, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        AddressDTO updatedAddressDTO = addressService.updateAddressById(addressId, addressDTO, user);
        return new ResponseEntity<>(updatedAddressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long addressId) {
        User user = authUtil.loggedInUser();
        return new ResponseEntity<>(addressService.deleteAddressById(addressId, user), HttpStatus.OK);
    }
}
