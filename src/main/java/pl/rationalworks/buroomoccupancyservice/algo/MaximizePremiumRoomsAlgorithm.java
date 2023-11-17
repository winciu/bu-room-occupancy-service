package pl.rationalworks.buroomoccupancyservice.algo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import pl.rationalworks.buroomoccupancyservice.model.Hotel;
import pl.rationalworks.buroomoccupancyservice.model.RoomType;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;

import static java.util.Arrays.stream;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.ECONOMY;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.PREMIUM;

@Component
@ConditionalOnProperty(
        value = "hotel.algo",
        havingValue = "maximizePremiumRooms")
public class MaximizePremiumRoomsAlgorithm extends RoomBookingAlgorithm {

    /**
     * Method used to book a hotel room for a specified price. Algorithm used in this method decides which standard of the room
     * by the price a client is going to offer for a room. <br/>
     * Clients who wants to pay below a given threshold value will get an economy room. Clients offering more than the
     * threshold value will get a premium standard of the room. They will never be offered an economy standard room.
     * However, clients willing to pay below the threshold (economy clients) will get a premium room if such rooms are
     * still available and their offer price for a room was higher than other prices below threshold.
     *
     * @param hotel        {@link Hotel} instance for which the booking should take place
     * @param economyPrices an array of sorted, reversed prices lower than a configurable threshold
     * @param premiumPrices an array of sorted, reversed prices higher or equal than a configurable threshold
     */
    @Override
    public void bookRooms(Hotel hotel, int[] economyPrices, int[] premiumPrices) {
        // start booking rooms for premium offers, start from the end (the higher price offered)
        stream(premiumPrices).forEach(price -> hotel.bookARoom(PREMIUM, price));

        // continue with economy prices, but offer a premium room if available
        // start from the higher prices
        stream(economyPrices)
                .forEach(price -> {
                    if (hotel.getAvailableRooms(PREMIUM) > 0) {
                        hotel.bookARoom(PREMIUM, price);
                    } else {
                        hotel.bookARoom(ECONOMY, price);
                    }
                });
    }
}
