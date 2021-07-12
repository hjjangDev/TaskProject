package com.apmall.dao.product;

import com.apmall.dto.product.ProductDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("productDAO")
public interface ProductDAO {

    List<ProductDto> selectProducts(Map<String, String> paramMap) throws Exception;

    int insertProduct(ProductDto productDto) throws Exception;

    int updateProduct(ProductDto productDto) throws Exception;

    int deleteProduct(ProductDto productDto) throws Exception;
}
