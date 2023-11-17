package pl.rationalworks.buroomoccupancyservice;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.rationalworks.buroomoccupancyservice.model.Hotel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.ECONOMY;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.PREMIUM;

@SpringBootTest
public class HotelServiceTest {

    @Autowired
    private HotelService hotelService;

    @Test
    void shouldSetupAHotelInstanceProperly() {
        Hotel hotel = hotelService.setupHotel(2, 12);
        assertEquals(2, hotel.getAvailableRooms(ECONOMY));
        assertEquals(12, hotel.getAvailableRooms(PREMIUM));
    }


}
