package pl.rationalworks.buroomoccupancyservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rationalworks.buroomoccupancyservice.algo.RoomBookingAlgorithm;
import pl.rationalworks.buroomoccupancyservice.model.Hotel;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;
import pl.rationalworks.buroomoccupancyservice.properties.HotelProperties;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final HotelProperties hotelProperties;
    private final RoomBookingAlgorithm bookingAlgorithm;

    public Hotel setupHotel(int economyRooms, int premiumRooms) {
        return new Hotel(economyRooms, premiumRooms, hotelProperties.getEconomyThreshold());
    }

    public void bookHotelRoomsForClients(Hotel hotel, ClientPrices clientPrices) {
        bookingAlgorithm.bookRooms(hotel, clientPrices);
    }
}
