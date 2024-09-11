package com.hermez.farrot.category.service.impl;

import com.hermez.farrot.category.entity.Category;
import com.hermez.farrot.category.repository.CategoryRepository;
import com.hermez.farrot.category.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}