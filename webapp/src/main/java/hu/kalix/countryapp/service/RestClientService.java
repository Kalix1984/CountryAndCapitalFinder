package hu.kalix.countryapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import static org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

import java.io.Serializable;
import java.util.List;


@Service
public class RestClientService implements Serializable {

    public static List<JsonNode> getCountryByName(String name) {
        final RequestHeadersSpec<?> spec = WebClient.create()
                                                    .get()
                                                    .uri(RestURLs.COUNTRY.getUrl() + name );

        final List<JsonNode> data = spec.retrieve()
                                          .toEntityList(JsonNode.class)
                                          .block()
                                          .getBody();

      return data;
    }

    public static List<JsonNode> getCountryByCapital(String name) {
        final RequestHeadersSpec<?> spec = WebClient.create()
                                                    .get()
                                                    .uri(RestURLs.CAPITAL.getUrl() + name);

        final List<JsonNode> data = spec.retrieve()
                                          .toEntityList(JsonNode.class)
                                          .block()
                                          .getBody();

        return data;
    }
}
