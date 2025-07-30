package ru.practicum.main.compilation.service;

import ru.practicum.main.compilation.dto.CompilationCreateDto;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.CompilationUpdateDto;

import java.util.Collection;

public interface CompilationService {
    CompilationDto addCompilation(CompilationCreateDto compilationCreateDto);

    CompilationDto updateCompilationByAdmin(CompilationUpdateDto compilationUpdateDto, Long compilationId);

    void removeCompilationById(Long compilationId);

    Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compilationId);
}
