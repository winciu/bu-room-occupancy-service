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

    @Test
    void clientCannotBookAnEconomyRoomPayingTooMuch() {
        Hotel hotel = new Hotel();
        hotel.setAvailableRooms(ECONOMY, 1);
        hotel.bookARoom(ECONOMY, 100);
        assertEquals(1, hotel.getAvailableRooms(ECONOMY));
    }

    @Test
    void clientCanBookARoomOfAGivenType() {
        Hotel hotel = new Hotel();
        hotel.setAvailableRooms(ECONOMY, 1);
        hotel.setAvailableRooms(PREMIUM,2);
        hotel.bookARoom(PREMIUM, 30);
        hotel.bookARoom(ECONOMY, 44);

        assertEquals(1, hotel.getAvailableRooms(PREMIUM));
        assertEquals(0, hotel.getAvailableRooms(ECONOMY));

        assertEquals(1, hotel.getNumberOfBookedRooms(PREMIUM));
        assertEquals(1, hotel.getNumberOfBookedRooms(ECONOMY));

        assertEquals(30, hotel.getValueOfBookedRooms(PREMIUM));
        assertEquals(44, hotel.getValueOfBookedRooms(ECONOMY));
    }

    @Test
    void afterBookingMoreThenOneRoomNumberAndValueOfBookedRoomsShouldMatch() {
        Hotel hotel = new Hotel();
        hotel.setAvailableRooms(ECONOMY, 2);
        hotel.bookARoom(ECONOMY, 30);
        hotel.bookARoom(ECONOMY, 44);

        assertEquals(0, hotel.getAvailableRooms(ECONOMY));
        assertEquals(2, hotel.getNumberOfBookedRooms(ECONOMY));
        assertEquals(74, hotel.getValueOfBookedRooms(ECONOMY));
    }

    @Test
    void userCannotBookMoreThanAvailableNumberOfRooms() {
        Hotel hotel = new Hotel();
        hotel.setAvailableRooms(ECONOMY, 1);
        hotel.bookARoom(ECONOMY, 26);
        hotel.bookARoom(ECONOMY, 44);

        assertEquals(0, hotel.getAvailableRooms(ECONOMY));
        assertEquals(1, hotel.getNumberOfBookedRooms(ECONOMY));
        assertEquals(26, hotel.getValueOfBookedRooms(ECONOMY));
    }

}
