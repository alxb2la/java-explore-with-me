package ru.practicum.main.util;

import ru.practicum.stats.dto.ViewStatsDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ViewStatsConverter {

    private ViewStatsConverter() {
        throw new UnsupportedOperationException();
    }

    public static Map<Long, Long> mapViewStatsDtos(List<ViewStatsDto> viewStatsDtos) {
        if ((viewStatsDtos == null) || (viewStatsDtos.isEmpty())) {
            return Map.of();
        }
        Map<Long, Long> hits = new HashMap<>();
        for (ViewStatsDto dto : viewStatsDtos) {
            Long eventId = getIdFromUri(dto.getUri());
            if (eventId != null) {
                hits.put(eventId, dto.getHits());
            }
        }
        return hits;
    }

    private static Long getIdFromUri(String uri) {
        try {
            String[] splitParts = uri.split("/");
            return Long.parseLong(splitParts[splitParts.length - 1]);
        } catch (Exception e) {
            return null;
        }
    }
}
