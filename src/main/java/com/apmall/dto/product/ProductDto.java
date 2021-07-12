package com.apmall.dto.product;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductDto {
    public String product_name;
    public int product_price;
    public String category_name;

}
