package ru.practicum.main.category.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.dto.CategoryFullDto;
import ru.practicum.main.category.service.CategoryService;

import java.util.Collection;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
        path = "/categories",
        produces = "application/json"
)
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryFullDto> getCategories(
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size
    ) {
        log.info("Запрос на получение списка Categories: from {}, size {}", from, size);
        Collection<CategoryFullDto> categories = categoryService.getCategories(from, size);
        log.info("Успешно сформирован список Categories размером {}", categories.size());
        return categories;
    }

    @GetMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryFullDto getCategoryById(@PathVariable(name = "catId") @NotNull Long categoryId) {
        log.info("Запрос на получение Category с id {}", categoryId);
        CategoryFullDto categoryFullDto = categoryService.getCategoryById(categoryId);
        log.info("Успешно получена Category: {}", categoryFullDto);
        return categoryFullDto;
    }
}
