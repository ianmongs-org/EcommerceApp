package com.java.EcomerceApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "street name must have a min of 5 characters")
    private String street;

    @NotBlank
    @Size(min = 5, message = "building name must have a min of 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 4, message = "city name must have a min of 4 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "state name must have a min of 2 characters")
    private String state;
    @NotBlank
    @Size(min = 2, message = "country name must have a min of 2 characters")
    private String country;

    @NotBlank
    @Size(min = 6, message = "pin code name must have a min of 6 characters")
    private String pinCode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

}
