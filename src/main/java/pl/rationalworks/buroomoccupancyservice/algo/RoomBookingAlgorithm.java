package pl.rationalworks.buroomoccupancyservice.algo;

import pl.rationalworks.buroomoccupancyservice.model.Hotel;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;

import static java.util.Arrays.sort;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.ArrayUtils.reverse;


public abstract class RoomBookingAlgorithm {
    public void bookRooms(Hotel hotel, ClientPrices clientPrices) {
        //sort the client prices array
        int[] prices = clientPrices.prices();
        sort(prices);
        // split this sorted array into two arrays based on an economy-standard threshold value.
        int[] economyPrices = stream(prices).filter(p -> p < hotel.getEconomyThresholdValue()).toArray();
        int[] premiumPrices = stream(prices).filter(p -> p >= hotel.getEconomyThresholdValue()).toArray();

        reverse(premiumPrices);
        reverse(economyPrices);

        bookRooms(hotel, economyPrices, premiumPrices);
    }

    public abstract void bookRooms(Hotel hotel, int[] economyPrices, int[] premiumPrices);

}
