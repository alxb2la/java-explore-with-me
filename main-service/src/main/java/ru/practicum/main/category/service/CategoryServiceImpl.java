package ru.practicum.main.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.Category;
import ru.practicum.main.category.CategoryMapper;
import ru.practicum.main.category.CategoryRepository;
import ru.practicum.main.category.dto.CategoryCreateDto;
import ru.practicum.main.category.dto.CategoryFullDto;
import ru.practicum.main.event.EventRepository;
import ru.practicum.main.exception.CategoryNameValidationException;
import ru.practicum.main.exception.ConflictCommonException;
import ru.practicum.main.exception.NotFoundException;

import java.util.Collection;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryFullDto addCategory(CategoryCreateDto categoryCreateDto) {
        try {
            Category category = categoryRepository.save(CategoryMapper.toCategory(categoryCreateDto));
            return CategoryMapper.toCategoryFullDto(category);
        } catch (DataIntegrityViolationException e) {
            throw new CategoryNameValidationException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public CategoryFullDto updateCategoryByAdmin(CategoryCreateDto categoryDto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found by id: " + categoryId));
        if (!category.getName().equals(categoryDto.getName())) {
            if (categoryRepository.findByName(categoryDto.getName()).isPresent()) {
                throw new CategoryNameValidationException("Category name conflict");
            }
        }
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return CategoryMapper.toCategoryFullDto(category);
    }

    @Override
    @Transactional
    public void removeCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found by id: " + categoryId));
        if (eventRepository.findFistByCategoryId(categoryId).isPresent()) {
            throw new ConflictCommonException("The category is not empty");
        }
        categoryRepository.delete(category);
    }

    @Override
    public Collection<CategoryFullDto> getCategories(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(from, size, Sort.by("id").ascending())).getContent()
                .stream()
                .map(CategoryMapper::toCategoryFullDto)
                .toList();
    }

    @Override
    public CategoryFullDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found by id: " + categoryId));
        return CategoryMapper.toCategoryFullDto(category);
    }
}
