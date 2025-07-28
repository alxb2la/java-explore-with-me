package ru.practicum.main.category.service;

import ru.practicum.main.category.dto.CategoryCreateDto;
import ru.practicum.main.category.dto.CategoryFullDto;

import java.util.Collection;

public interface CategoryService {
    CategoryFullDto addCategory(CategoryCreateDto categoryCreateDto);

    CategoryFullDto updateCategoryByAdmin(CategoryCreateDto categoryDto, Long categoryId);

    void removeCategoryById(Long categoryId);

    Collection<CategoryFullDto> getCategories(Integer from, Integer size);

    CategoryFullDto getCategoryById(Long categoryId);
}
