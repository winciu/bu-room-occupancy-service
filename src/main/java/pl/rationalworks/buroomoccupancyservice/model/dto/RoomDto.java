package pl.rationalworks.buroomoccupancyservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.rationalworks.buroomoccupancyservice.model.RoomType;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RoomDto(RoomType roomType, int available, int booked, int totalPrice) {

}
