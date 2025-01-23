package com.java.EcomerceApp.controller;

import com.java.EcomerceApp.dto.AddressDTO;
import com.java.EcomerceApp.model.User;
import com.java.EcomerceApp.security.utils.AuthUtil;
import com.java.EcomerceApp.service.address.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Address", description = "Address management APIs")
public class AddressController {

    private final AuthUtil authUtil;
    private final AddressService addressService;

    public AddressController(AuthUtil authUtil, AddressService addressService) {
        this.authUtil = authUtil;
        this.addressService = addressService;
    }

    @PostMapping("/address")
    @Operation(summary = "Create a new address", description = "Create a new address for the logged-in user")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    @Operation(summary = "Get all addresses", description = "Retrieve all addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        List<AddressDTO> addressDTOList = addressService.getAddress();
        return new ResponseEntity<>(addressDTOList, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    @Operation(summary = "Get address by ID", description = "Retrieve an address by its ID")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    @Operation(summary = "Get user addresses", description = "Retrieve all addresses for the logged-in user")
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {
        User user = authUtil.loggedInUser();
        List<AddressDTO> addressDTOs = addressService.getUserAddresses(user);
        return new ResponseEntity<>(addressDTOs, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    @Operation(summary = "Update address by ID", description = "Update an address by its ID")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        AddressDTO updatedAddressDTO = addressService.updateAddressById(addressId, addressDTO, user);
        return new ResponseEntity<>(updatedAddressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    @Operation(summary = "Delete address by ID", description = "Delete an address by its ID")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long addressId) {
        User user = authUtil.loggedInUser();
        return new ResponseEntity<>(addressService.deleteAddressById(addressId, user), HttpStatus.OK);
    }
}