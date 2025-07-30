package ru.practicum.main.compilation.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.service.CompilationService;

import java.util.Collection;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(
        path = "/compilations",
        produces = "application/json"
)
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    public Collection<CompilationDto> getCompilations(
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
            @RequestParam(name = "pinned", required = false) Boolean pinned
    ) {
        log.info("Запрос на получение списка Compilations: from {}, size {}, pinned {}", from, size, pinned);
        Collection<CompilationDto> compilations = compilationService.getCompilations(pinned, from, size);
        log.info("Успешно сформирован список Compilations размером {}", compilations.size());
        return compilations;
    }

    @GetMapping(path = "/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable(name = "compId") @NotNull Long compilationId) {
        log.info("Запрос на получение Compilation с id {}", compilationId);
        CompilationDto compilationDto = compilationService.getCompilationById(compilationId);
        log.info("Успешно получена Category: {}", compilationDto);
        return compilationDto;
    }
}
