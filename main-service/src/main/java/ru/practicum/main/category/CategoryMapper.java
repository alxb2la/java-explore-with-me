package ru.practicum.main.category;

import ru.practicum.main.category.dto.CategoryCreateDto;
import ru.practicum.main.category.dto.CategoryFullDto;

public final class CategoryMapper {

    private CategoryMapper() {
        throw new UnsupportedOperationException();
    }

    public static Category toCategory(CategoryCreateDto categoryCreateDto) {
        return Category.builder()
                .id(null)
                .name(categoryCreateDto.getName())
                .build();
    }

    public static CategoryFullDto toCategoryFullDto(Category category) {
        return CategoryFullDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
