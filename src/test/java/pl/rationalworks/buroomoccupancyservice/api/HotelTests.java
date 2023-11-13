package pl.rationalworks.buroomoccupancyservice.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.rationalworks.buroomoccupancyservice.api.RoomType.ECONOMY;
import static pl.rationalworks.buroomoccupancyservice.api.RoomType.PREMIUM;

public class HotelTests {

    @Test
    void shouldSetRoomAvailabilityForAHotel() {
        Hotel hotel = new Hotel();
        hotel.setAvailableRooms(ECONOMY, 1);
        hotel.setAvailableRooms(PREMIUM, 3);

        assertEquals(1, hotel.getAvailableRooms(ECONOMY));
        assertEquals(3, hotel.getAvailableRooms(PREMIUM));
    }

    @Test
    void byDefaultHotelHasNoRoomsOfAnyType() {
        Hotel hotel = new Hotel();
        assertEquals(0, hotel.getAvailableRooms(ECONOMY));
        assertEquals(0, hotel.getAvailableRooms(PREMIUM));
    }
}
