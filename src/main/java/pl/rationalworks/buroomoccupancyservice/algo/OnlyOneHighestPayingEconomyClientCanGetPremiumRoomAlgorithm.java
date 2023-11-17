package pl.rationalworks.buroomoccupancyservice.algo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import pl.rationalworks.buroomoccupancyservice.model.Hotel;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;

@Component
@ConditionalOnProperty(
        value = "hotel.algo",
        havingValue = "onlyOneHighestPayingEconomyCanGetPremium")
public class OnlyOneHighestPayingEconomyClientCanGetPremiumRoomAlgorithm implements RoomBookingAlgorithm {
    @Override
    public void bookRooms(Hotel hotel, ClientPrices prices) {

    }
}
