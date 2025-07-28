package ru.practicum.main.compilation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.dto.CompilationCreateDto;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.CompilationUpdateDto;
import ru.practicum.main.compilation.service.CompilationService;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
        path = "/admin/compilations",
        produces = "application/json"
)
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid CompilationCreateDto compilationCreateDto) {
        log.info("Запрос на добавление Compilation: {}", compilationCreateDto);
        CompilationDto compilationDto = compilationService.addCompilation(compilationCreateDto);
        log.info("Успешно добавлена Compilation: {}", compilationDto);
        return compilationDto;
    }

    @PatchMapping(
            path = "/{compId}",
            consumes = "application/json"
    )
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilationByAdmin(@PathVariable(name = "compId") @NotNull Long compilationId,
                                                   @RequestBody @Valid CompilationUpdateDto compilationUpdateDto) {
        log.info("Запрос на обновление Compilation с id {}", compilationId);
        CompilationDto compilationDto = compilationService.updateCompilationByAdmin(compilationUpdateDto, compilationId);
        log.info("Успешно обновлена Compilation: {}", compilationDto);
        return compilationDto;
    }

    @DeleteMapping(path = "/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCompilationById(@PathVariable(name = "compId") @NotNull Long compilationId) {
        log.info("Запрос на удаление Compilation по id: {}", compilationId);
        compilationService.removeCompilationById(compilationId);
        log.info("Успешно удалена Compilation с id: {}", compilationId);
    }
}
