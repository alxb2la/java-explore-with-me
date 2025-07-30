package ru.practicum.main.category.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.category.dto.CategoryCreateDto;
import ru.practicum.main.category.dto.CategoryFullDto;
import ru.practicum.main.category.service.CategoryService;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
        path = "/admin/categories",
        produces = "application/json"
)
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryFullDto addCategory(@RequestBody @Valid CategoryCreateDto categoryCreateDto) {
        log.info("Запрос на добавление Category: {}", categoryCreateDto);
        CategoryFullDto categoryFullDto = categoryService.addCategory(categoryCreateDto);
        log.info("Успешно добавлена Category: {}", categoryFullDto);
        return categoryFullDto;
    }

    @PatchMapping(
            path = "/{catId}",
            consumes = "application/json"
    )
    @ResponseStatus(HttpStatus.OK)
    public CategoryFullDto updateCategoryByAdmin(@PathVariable(name = "catId") @NotNull Long categoryId,
                                                 @RequestBody @Valid CategoryCreateDto categoryDto) {
        log.info("Запрос на обновление Category с id {}", categoryId);
        CategoryFullDto categoryFullDto = categoryService.updateCategoryByAdmin(categoryDto, categoryId);
        log.info("Успешно обновлена Category: {}", categoryFullDto);
        return categoryFullDto;
    }

    @DeleteMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategoryById(@PathVariable(name = "catId") @NotNull Long categoryId) {
        log.info("Запрос на удаление Category по id: {}", categoryId);
        categoryService.removeCategoryById(categoryId);
        log.info("Успешно удалена Category с id: {}", categoryId);
    }
}
