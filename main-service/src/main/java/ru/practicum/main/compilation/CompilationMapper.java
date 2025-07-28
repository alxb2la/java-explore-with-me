package ru.practicum.main.compilation;

import ru.practicum.main.compilation.dto.CompilationCreateDto;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.model.Event;

import java.util.List;

public final class CompilationMapper {

    private CompilationMapper() {
        throw new UnsupportedOperationException();
    }

    public static Compilation toCompilation(CompilationCreateDto compilationCreateDto, List<Event> events) {
        return Compilation.builder()
                .id(null)
                .title(compilationCreateDto.getTitle())
                .pinned(compilationCreateDto.getPinned())
                .events(events)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(events)
                .build();
    }
}
