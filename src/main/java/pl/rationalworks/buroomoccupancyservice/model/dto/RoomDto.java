package pl.rationalworks.buroomoccupancyservice.model.dto;

import pl.rationalworks.buroomoccupancyservice.model.RoomType;

public record RoomDto(RoomType roomType, int available, int booked, int totalPrice) {

}
