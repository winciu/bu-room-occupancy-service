package pl.rationalworks.buroomoccupancyservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HotelDto(@JsonProperty Set<RoomDto> rooms, @JsonProperty int economyRoomPriceThreshold) {
}
