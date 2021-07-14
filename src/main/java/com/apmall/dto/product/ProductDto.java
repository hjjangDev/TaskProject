package com.apmall.dto.product;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductDto {

    public String product_no;
    public String product_name;
    public String brand_name;
    public String product_price;
    public String category_no;
    public String category_name;

}
