package pl.rationalworks.buroomoccupancyservice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rationalworks.buroomoccupancyservice.algo.RoomBookingAlgorithm;
import pl.rationalworks.buroomoccupancyservice.model.Hotel;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;
import pl.rationalworks.buroomoccupancyservice.properties.HotelProperties;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelProperties hotelProperties;
    private final RoomBookingAlgorithm bookingAlgorithm;

    /**
     * TODO: to be removed
     * Initialization and getter method just for the sake of initial tests. To be removed in production code.
     * In production, there will be a hotel object already created
     */
    @Getter
    private Hotel hotel = new Hotel(0, 0, 0);

    public Hotel setupHotel(int economyRooms, int premiumRooms) {
        // an imitation of a DB logic
        this.hotel = new Hotel(economyRooms, premiumRooms, hotelProperties.getEconomyThreshold());
        return hotel;
    }

    public Hotel bookHotelRoomsForClients(ClientPrices clientPrices) {
        bookingAlgorithm.bookRooms(hotel, clientPrices);
        return hotel;
    }
}
