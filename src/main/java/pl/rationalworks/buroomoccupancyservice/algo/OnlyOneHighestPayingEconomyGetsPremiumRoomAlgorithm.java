package pl.rationalworks.buroomoccupancyservice.algo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import pl.rationalworks.buroomoccupancyservice.model.Hotel;

import static java.util.Arrays.stream;
import static org.apache.commons.lang3.ArrayUtils.subarray;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.ECONOMY;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.PREMIUM;

@Component
@ConditionalOnProperty(
        value = "hotel.algo",
        havingValue = "onlyOneHighestPayingEconomyGetsPremium",
        matchIfMissing = true)
public class OnlyOneHighestPayingEconomyGetsPremiumRoomAlgorithm extends RoomBookingAlgorithm {

    /**
     * Method used to book a hotel room for a specified price. Algorithm used in this method decides a standard of the room
     * by the price a client is going to offer for a room. <br/>
     * Clients who wants to pay below a given threshold value will get an economy room. Clients offering more than the
     * threshold value will get a premium standard of the room. They will never be offered an economy standard room.
     * However, *only one* client willing to pay below the threshold (economy clients) will get a premium room if such room is
     * still available and his offer price for a room was the highest price below threshold.
     *
     * @param hotel        {@link Hotel} instance for which the booking should take place
     * @param economyPrices an array of sorted, reversed prices lower than a configurable threshold
     * @param premiumPrices an array of sorted, reversed prices higher or equal than a configurable threshold
     */
    @Override
    public void bookRooms(Hotel hotel, int[] economyPrices, int[] premiumPrices) {
        // start booking rooms for premium offers, start from the end (the higher price offered)
        stream(premiumPrices).forEach(price -> hotel.bookARoom(PREMIUM, price));

        // evaluate economy "upgrade" possibility
        // 1. if there is enough economy rooms to put all economy clients, go for it
        // 2. if above is not true, check remaining available premium rooms and book just one highest economy to premium
        // 3. book rest of economy into available economy rooms

        boolean enoughEconomyRoomsForAll = hotel.getAvailableRooms(ECONOMY) >= economyPrices.length;
        if (enoughEconomyRoomsForAll) {
            stream(economyPrices)
                    .forEach(price -> {
                        hotel.bookARoom(ECONOMY, price);
                    });
        } else {
            boolean isPremiumRoomAvailable = hotel.getAvailableRooms(PREMIUM) > 0;
            if (isPremiumRoomAvailable) {
                if (economyPrices.length > 0) {
                    int firstHighestEconomyPrice = economyPrices[0];
                    hotel.bookARoom(PREMIUM, firstHighestEconomyPrice);
                    //exclude the 1st highest price from economy prices for further processing
                    economyPrices = subarray(economyPrices, 1, economyPrices.length);
                }
            }
            // put the rest economy into available economy rooms
            stream(economyPrices)
                    .forEach(price -> {
                        hotel.bookARoom(ECONOMY, price);
                    });
        }
    }
}
