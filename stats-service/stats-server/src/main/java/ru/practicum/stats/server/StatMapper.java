package ru.practicum.stats.server;

import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.model.Stat;
import ru.practicum.stats.server.model.ViewStats;

public final class StatMapper {

    private StatMapper() {
        throw new UnsupportedOperationException();
    }

    public static Stat toStat(EndpointHitDto statCreateDto) {
        return Stat.builder()
                .id(null)
                .app(statCreateDto.getApp())
                .uri(statCreateDto.getUri())
                .ip(statCreateDto.getIp())
                .timestamp(statCreateDto.getTimestamp())
                .build();
    }

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }
}
