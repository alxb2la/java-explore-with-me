package ru.practicum.stats.dto;

import lombok.*;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Builder
public class ViewStatsDto {
    private String app;
    private String uri;
    private Long hits;
}
