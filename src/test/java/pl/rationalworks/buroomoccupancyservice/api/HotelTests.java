package pl.rationalworks.buroomoccupancyservice.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.rationalworks.buroomoccupancyservice.api.RoomType.ECONOMY;
import static pl.rationalworks.buroomoccupancyservice.api.RoomType.PREMIUM;

public class HotelTests {

    private static Hotel createHotelWithAvailableRooms(RoomType[] roomTypes, Integer... availableRooms) {
        Hotel hotel = new Hotel();
        for (int i = 0; i < Math.min(roomTypes.length, availableRooms.length); i++) {
            hotel.setAvailableRooms(roomTypes[i], availableRooms[i]);
        }
        return hotel;
    }

    private static void bookRooms(Hotel hotel, RoomType[] roomTypes, Integer... prices) {
        for (int i = 0; i < Math.min(roomTypes.length, prices.length); i++) {
            hotel.bookARoom(roomTypes[i], prices[i]);
        }
    }

    private static RoomType[] roomTypes(RoomType... roomTypes) {
        return roomTypes;
    }

    private static void assertAvailabilityOfRooms(Hotel hotel, RoomType[] roomTypes, int... availabilityOfRooms) {
        for (int i = 0; i < Math.min(roomTypes.length, availabilityOfRooms.length); i++) {
            assertEquals(availabilityOfRooms[i], hotel.getAvailableRooms(roomTypes[i]));
        }
    }

    private static void assertNumberOfBookedRooms(Hotel hotel, RoomType[] roomTypes, int... roomsBooked) {
        for (int i = 0; i < Math.min(roomTypes.length, roomsBooked.length); i++) {
            assertEquals(roomsBooked[i], hotel.getNumberOfBookedRooms(roomTypes[i]));
        }
    }

    private static void assertValueOfBookedRooms(Hotel hotel, RoomType[] roomTypes, int... roomPrices) {
        for (int i = 0; i < Math.min(roomTypes.length, roomPrices.length); i++) {
            assertEquals(roomPrices[i], hotel.getValueOfBookedRooms(roomTypes[i]));
        }
    }

    @Test
    void shouldSetRoomAvailabilityForAHotel() {
        Hotel hotel = createHotelWithAvailableRooms(roomTypes(ECONOMY, PREMIUM), 1, 3);

        assertAvailabilityOfRooms(hotel, roomTypes(ECONOMY, PREMIUM), 1, 3);
    }

    @Test
    void byDefaultHotelHasNoRoomsOfAnyType() {
        Hotel hotel = createHotelWithAvailableRooms(roomTypes());

        assertAvailabilityOfRooms(hotel, roomTypes(ECONOMY, PREMIUM), 0, 0);
    }

    @Test
    void clientCannotBookAnEconomyRoomPayingTooMuch() {
        Hotel hotel = createHotelWithAvailableRooms(roomTypes(ECONOMY), 1);
        bookRooms(hotel, roomTypes(ECONOMY), 100);

        assertAvailabilityOfRooms(hotel, roomTypes(ECONOMY), 1);
    }

    @Test
    void clientCanBookARoomOfAGivenType() {
        Hotel hotel = createHotelWithAvailableRooms(roomTypes(ECONOMY, PREMIUM), 1, 2);
        bookRooms(hotel, roomTypes(PREMIUM, ECONOMY), 30, 44);

        assertAvailabilityOfRooms(hotel, roomTypes(ECONOMY, PREMIUM), 0, 1);
        assertNumberOfBookedRooms(hotel, roomTypes(ECONOMY, PREMIUM), 1, 1);
        assertValueOfBookedRooms(hotel, roomTypes(ECONOMY, PREMIUM), 44, 30);
    }

    @Test
    void afterBookingMoreThenOneRoomNumberAndValueOfBookedRoomsShouldMatch() {
        Hotel hotel = createHotelWithAvailableRooms(roomTypes(ECONOMY), 2);
        bookRooms(hotel, roomTypes(ECONOMY, ECONOMY), 30, 44);

        assertAvailabilityOfRooms(hotel, roomTypes(ECONOMY), 0);
        assertNumberOfBookedRooms(hotel, roomTypes(ECONOMY), 2);
        assertValueOfBookedRooms(hotel, roomTypes(ECONOMY), 74);
    }

    @Test
    void itIsNotPossibleToBookMoreThanAvailableNumberOfRooms() {
        Hotel hotel = createHotelWithAvailableRooms(roomTypes(ECONOMY), 1);
        bookRooms(hotel, roomTypes(ECONOMY, ECONOMY), 26, 44);

        assertAvailabilityOfRooms(hotel, roomTypes(ECONOMY), 0);
        assertNumberOfBookedRooms(hotel, roomTypes(ECONOMY), 1);
        assertValueOfBookedRooms(hotel, roomTypes(ECONOMY), 26);
    }

}
