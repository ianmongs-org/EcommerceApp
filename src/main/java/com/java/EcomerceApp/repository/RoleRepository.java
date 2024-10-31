package com.java.EcomerceApp.repository;

import com.java.EcomerceApp.model.AppRole;
import com.java.EcomerceApp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAppRole(AppRole name);
}
