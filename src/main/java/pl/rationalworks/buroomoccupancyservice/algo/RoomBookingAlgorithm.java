package pl.rationalworks.buroomoccupancyservice.algo;

import pl.rationalworks.buroomoccupancyservice.model.Hotel;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;

@FunctionalInterface
public interface RoomBookingAlgorithm {
    void bookRooms(Hotel hotel, ClientPrices prices);
}
