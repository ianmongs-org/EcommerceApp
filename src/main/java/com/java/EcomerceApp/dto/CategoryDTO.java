package com.java.EcomerceApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;
}
