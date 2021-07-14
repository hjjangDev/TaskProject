package com.apmall.dao.product;

import com.apmall.dto.product.ProductDto;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository("productDAO")
public interface ProductDAO {

    List<ProductDto> selectProducts(HashMap<String, String> paramMap) throws Exception;

    int insertProduct(ProductDto productDto) throws Exception;

    int updateProduct(ProductDto productDto) throws Exception;

    int deleteProduct(ProductDto productDto) throws Exception;
}
