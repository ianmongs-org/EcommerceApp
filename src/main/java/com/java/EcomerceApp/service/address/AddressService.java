package com.java.EcomerceApp.service.address;

import com.java.EcomerceApp.dto.AddressDTO;
import com.java.EcomerceApp.model.User;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddress();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO, User user);

    String deleteAddressById(Long addressId, User user);
}
