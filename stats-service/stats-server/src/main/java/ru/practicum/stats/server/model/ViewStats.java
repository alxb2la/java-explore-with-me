package ru.practicum.stats.server.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Builder
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}
