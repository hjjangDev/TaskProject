package com.apmall.service.category.impl;

import com.apmall.dao.category.CategoryDAO;
import com.apmall.dto.category.CategoryDto;
import com.apmall.service.category.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryServiceImpl(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public List<CategoryDto> selectCategories() throws Exception {
        return categoryDAO.selectCategories();
    }

    @Override
    public int insertCategory(CategoryDto categoryDto) throws Exception {
        return categoryDAO.insertCategory(categoryDto);
    }

    @Override
    public int updateCategory(CategoryDto categoryDto) throws Exception {
        return categoryDAO.updateCategory(categoryDto);
    }

    @Override
    public int deleteCategory(CategoryDto categoryDto) throws Exception {
        return categoryDAO.deleteCategory(categoryDto);
    }

}
