package pl.rationalworks.buroomoccupancyservice.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientPrices(@JsonValue int[] prices) {

    @JsonCreator
    public ClientPrices {
    }
}
