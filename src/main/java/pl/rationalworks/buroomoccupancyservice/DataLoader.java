package pl.rationalworks.buroomoccupancyservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;
import pl.rationalworks.buroomoccupancyservice.properties.DataLoaderProperties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader {

    private final ObjectMapper objectMapper;
    private final DataLoaderProperties properties;
    private final RestTemplate restTemplate;
    private boolean dataLoaded;
    private ClientPrices clientPrices;

    public ClientPrices loadData() throws IOException {
        if (dataLoaded) {
            log.trace("Data already loaded");
            return clientPrices;
        }
        switch (properties.getMode()) {
            case "local" -> loadData(properties.getSourceLocal());
            case "remote" -> loadData(properties.getSourceRemote());
            default -> throw new IllegalArgumentException("Unknown data source mode: " + properties.getMode());
        }

        dataLoaded = true;
        return clientPrices;
    }

    private void loadData(URI sourceRemote) {
        log.info("Data loaded from remote source {}", sourceRemote);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ClientPrices> response = restTemplate.exchange(sourceRemote, HttpMethod.GET, entity, ClientPrices.class);
        clientPrices = response.getBody();
    }

    private void loadData(String dataFilePath) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(dataFilePath)) {
            int[] prices = objectMapper.readValue(inputStream, int[].class);
            clientPrices = new ClientPrices(prices);
        }
    }

}
