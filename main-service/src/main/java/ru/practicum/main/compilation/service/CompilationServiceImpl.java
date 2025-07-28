package ru.practicum.main.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.compilation.Compilation;
import ru.practicum.main.compilation.CompilationMapper;
import ru.practicum.main.compilation.CompilationRepository;
import ru.practicum.main.compilation.dto.CompilationCreateDto;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.CompilationUpdateDto;
import ru.practicum.main.event.EventMapper;
import ru.practicum.main.event.EventRepository;
import ru.practicum.main.event.dto.EventShortDto;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.exception.CompilationTitleValidationException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.ValidationException;
import ru.practicum.main.util.ViewStatsConverter;
import ru.practicum.stats.client.StatClient;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final StatClient statClient;

    @Override
    @Transactional
    public CompilationDto addCompilation(CompilationCreateDto compilationCreateDto) {
        if (compilationRepository.findByTitle(compilationCreateDto.getTitle()).isPresent()) {
            throw new CompilationTitleValidationException("Compilation with the specified title already exists");
        }
        if (compilationCreateDto.getPinned() == null) {
            compilationCreateDto.setPinned(false);
        }
        List<Event> events;
        List<Long> eventIds = compilationCreateDto.getEvents();
        if (eventIds != null) {
            if (eventIds.size() != eventIds.stream().distinct().count()) {
                throw new ValidationException("The event IDs in the collection have duplicates.");
            }
            eventIds = eventIds.stream()
                    .filter(Objects::nonNull)
                    .toList();
            events = eventRepository.findByIdInOrderByIdAsc(eventIds);
        } else {
            events = List.of();
        }

        Compilation compilation =
                compilationRepository.save(CompilationMapper.toCompilation(compilationCreateDto, events));
        if (events.isEmpty()) {
            return CompilationMapper.toCompilationDto(compilation, List.of());
        }

        // Get views
        List<String> uris = events.stream()
                .map(e -> "/events/" + e.getId())
                .toList();
        List<ViewStatsDto> viewStats = statClient.getViewStats(
                events.getFirst().getCreatedOn(),
                LocalDateTime.now(),
                uris,
                true
        );
        Map<Long, Long> views = ViewStatsConverter.mapViewStatsDtos(viewStats);
        List<EventShortDto> eventShortDtos = events.stream()
                .map(e -> EventMapper.toEventShortDto(e, views.getOrDefault(e.getId(), 0L)))
                .toList();

        return CompilationMapper.toCompilationDto(compilation, eventShortDtos);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilationByAdmin(CompilationUpdateDto compilationUpdateDto, Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation not found by id: " + compilationId));

        if (compilationUpdateDto.getTitle() != null) {
            if (!compilation.getTitle().equalsIgnoreCase(compilationUpdateDto.getTitle())) {
                if (compilationRepository.findByTitle(compilationUpdateDto.getTitle()).isPresent()) {
                    throw new CompilationTitleValidationException("Compilation with the specified title already exists");
                }
            }
            compilation.setTitle(compilationUpdateDto.getTitle());
        }
        if (compilationUpdateDto.getPinned() != null) {
            compilation.setPinned(compilationUpdateDto.getPinned());
        }
        List<Long> eventIds = compilationUpdateDto.getEvents();
        if (eventIds != null) {
            if (eventIds.size() != eventIds.stream().distinct().count()) {
                throw new ValidationException("The event IDs in the collection have duplicates.");
            }
            eventIds = eventIds.stream()
                    .filter(Objects::nonNull)
                    .toList();
            List<Event> events = eventRepository.findByIdInOrderByIdAsc(eventIds);
            compilation.setEvents(events);
        }

        compilationRepository.save(compilation);
        if (compilation.getEvents().isEmpty()) {
            return CompilationMapper.toCompilationDto(compilation, List.of());
        }

        // Get views
        List<String> uris = compilation.getEvents().stream()
                .map(e -> "/events/" + e.getId())
                .toList();
        List<ViewStatsDto> viewStats = statClient.getViewStats(
                compilation.getEvents().getFirst().getCreatedOn(),
                LocalDateTime.now(),
                uris,
                true
        );
        Map<Long, Long> views = ViewStatsConverter.mapViewStatsDtos(viewStats);
        List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                .map(e -> EventMapper.toEventShortDto(e, views.getOrDefault(e.getId(), 0L)))
                .toList();

        return CompilationMapper.toCompilationDto(compilation, eventShortDtos);
    }

    @Override
    @Transactional
    public void removeCompilationById(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation not found by id: " + compilationId));
        compilationRepository.delete(compilation);
    }

    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("id").ascending());
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(pageable).getContent();
        } else {
            compilations = compilationRepository.findAllByPinned(pinned, pageable).getContent();
        }

        if (compilations.isEmpty()) {
            return List.of();
        }

        List<CompilationDto> compilationDtos = new ArrayList<>();
        for (Compilation compilation : compilations) {

            if (compilation.getEvents().isEmpty()) {
                compilationDtos.add(CompilationMapper.toCompilationDto(compilation, List.of()));
            } else {
                // Get views
                List<String> uris = compilation.getEvents().stream()
                        .map(e -> "/events/" + e.getId())
                        .toList();
                List<ViewStatsDto> viewStats = statClient.getViewStats(
                        compilation.getEvents().getFirst().getCreatedOn(),
                        LocalDateTime.now(),
                        uris,
                        true
                );
                Map<Long, Long> views = ViewStatsConverter.mapViewStatsDtos(viewStats);
                List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                        .map(e -> EventMapper.toEventShortDto(e, views.getOrDefault(e.getId(), 0L)))
                        .toList();
                compilationDtos.add(CompilationMapper.toCompilationDto(compilation, eventShortDtos));
            }
        }
        return compilationDtos;
    }

    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation not found by id: " + compilationId));

        if (compilation.getEvents().isEmpty()) {
            return CompilationMapper.toCompilationDto(compilation, List.of());
        }

        // Get views
        List<String> uris = compilation.getEvents().stream()
                .map(e -> "/events/" + e.getId())
                .toList();
        List<ViewStatsDto> viewStats = statClient.getViewStats(
                compilation.getEvents().getFirst().getCreatedOn(),
                LocalDateTime.now(),
                uris,
                true
        );
        Map<Long, Long> views = ViewStatsConverter.mapViewStatsDtos(viewStats);
        List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                .map(e -> EventMapper.toEventShortDto(e, views.getOrDefault(e.getId(), 0L)))
                .toList();

        return CompilationMapper.toCompilationDto(compilation, eventShortDtos);
    }
}
