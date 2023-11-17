package pl.rationalworks.buroomoccupancyservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rationalworks.buroomoccupancyservice.model.Hotel;
import pl.rationalworks.buroomoccupancyservice.model.RoomType;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;
import pl.rationalworks.buroomoccupancyservice.properties.HotelProperties;

import static java.util.Arrays.sort;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.ArrayUtils.reverse;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final HotelProperties hotelProperties;

    public Hotel setupHotel(int economyRooms, int premiumRooms) {
        return new Hotel(economyRooms, premiumRooms, hotelProperties.getEconomyThreshold());
    }

    /**
     * Method used to book a hotel room for a specified price. Algorithm used in this method decides which standard of the room
     * by the price a client is going to offer for a room. <br/>
     * Clients who wants to pay below a given threshold value will get an economy room. Clients offering more than the
     * threshold value will get a premium standard of the room. They will never be offered an economy standard room.
     * However, clients willing to pay below the threshold will get a premium room if such rooms are
     * still available and their offer price for a room was the higher than the prices below threshold.
     *
     * @param hotel        {@link Hotel} instance for which the booking should take place
     * @param clientPrices an instance of {@link ClientPrices} containing client prices they want to pay for a room
     */
    public void bookHotelRoomsForClients(Hotel hotel, ClientPrices clientPrices) {
        //sort the client prices array
        int[] prices = clientPrices.prices();
        sort(prices);
        // split this sorted array into two arrays based on an economy-standard threshold value.
        int thresholdValue = hotelProperties.getEconomyThreshold();
        int[] economyPrices = stream(prices).filter(p -> p < thresholdValue).toArray();
        int[] premiumPrices = stream(prices).filter(p -> p >= thresholdValue).toArray();

        // start booking rooms from premium offers, start from the end (the higher price)
        reverse(premiumPrices);
        stream(premiumPrices).forEach(price -> hotel.bookARoom(RoomType.PREMIUM, price));

        // continue with economy prices, but offer a premium room if available
        // start from the higher prices
        reverse(economyPrices);
        stream(economyPrices)
                .forEach(price -> {
                    if (hotel.getAvailableRooms(RoomType.PREMIUM) > 0) {
                        hotel.bookARoom(RoomType.PREMIUM, price);
                    } else {
                        hotel.bookARoom(RoomType.ECONOMY, price);
                    }
                });
    }
}
