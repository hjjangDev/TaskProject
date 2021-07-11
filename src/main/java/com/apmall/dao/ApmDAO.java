package com.apmall.dao;

import com.apmall.dto.CategoryDto;
import com.apmall.dto.ProductDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("apmDAO")
public interface ApmDAO {

    List<CategoryDto> selectCategories() throws Exception;
    List<ProductDto> selectProducts(ProductDto productDto) throws Exception;

    int insertCategory(Map<String, String> paramMap) throws Exception;
    int updateCategory(Map<String, String> paramMap) throws Exception;
    int deleteCategory(Map<String, String> paramMap) throws Exception;

    int insertProduct(Map<String, String> paramMap) throws Exception;
    int updateProduct(Map<String, String> paramMap) throws Exception;
    int deleteProduct(Map<String, String> paramMap) throws Exception;
}
