package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.HitRequestDto;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StatsClient extends BaseClient {
    private static final String API_PREFIX_HIT = "/hit";
    private static final String API_PREFIX_START = "/start";
    private static final String SERVER_URL = "http://localhost:9090";

    @Autowired
    public StatsClient(@Value(SERVER_URL) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postEndpointHit(HitRequestDto hitRequestDto) {
        return post(API_PREFIX_HIT, hitRequestDto);
    }

    public ResponseEntity<Object> getStatistic(String start, String end,
                                               List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );

        return get(API_PREFIX_START + "?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}