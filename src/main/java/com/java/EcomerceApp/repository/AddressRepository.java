package com.java.EcomerceApp.repository;

import com.java.EcomerceApp.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
