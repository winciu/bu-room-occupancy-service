package pl.rationalworks.buroomoccupancyservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record HotelDto(@JsonProperty Set<RoomDto> rooms, @JsonProperty int economyRoomPriceThreshold) {
}
