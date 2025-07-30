package ru.practicum.stats.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatClient {
    private final RestClient restClient;
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public void addStat(EndpointHitDto endpointHitDto) {
        restClient.post()
                .uri("/hit")
                .body(endpointHitDto)
                .retrieve()
                .body(EndpointHitDto.class);
    }

    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("/stats")
                .queryParam("start", start.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .queryParam("end", end.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)))
                .queryParam("unique", unique);
        if (uris != null && !uris.isEmpty()) {
            uris.forEach(uri -> uriBuilder.queryParam("uris", uri));
        }
        String uri = uriBuilder.build().toUriString();
        ResponseEntity<List<ViewStatsDto>> response = restClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }
}
