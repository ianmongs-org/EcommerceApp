package com.java.EcomerceApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @NotBlank
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    List<Product> products;
}
