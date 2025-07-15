package ru.practicum.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.StatMapper;
import ru.practicum.stats.server.StatRepository;
import ru.practicum.stats.server.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    @Transactional
    public void addStat(EndpointHitDto endpointHitDto) {
        statRepository.save(StatMapper.toStat(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> viewStats;
        if (unique) {
            viewStats = statRepository.findViewStatsUniqueIp(start, end, uris);
        } else {
            viewStats = statRepository.findViewStatsNotUniqueIp(start, end, uris);
        }
        return viewStats.stream()
                .map(StatMapper::toViewStatsDto)
                .sorted((vsd1, vsd2) -> Long.compare(vsd2.getHits(), vsd1.getHits()))
                .collect(Collectors.toList());
    }
}
