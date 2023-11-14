package pl.rationalworks.buroomoccupancyservice.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.ECONOMY;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.PREMIUM;

public class HotelTests {

    private static final int ECONOMY_THRESHOLD = 100;

    private static Hotel createHotelWithAvailableRooms(int economyRooms, int premiumRooms) {
        return new Hotel(economyRooms, premiumRooms, ECONOMY_THRESHOLD);
    }

    /**
     * Utility method for booking rooms in a {@link Hotel} instance. When using this method you need to obey a convention
     * by specifying its input arguments. Both input parameters of this method are arrays where items with the same
     * index forms a logical relation. <br/><br/>
     * For instance, when {@code roomTypes = [ECONOMIC, PREMIUM]}  and {@code prices = [20, 50]} it means that
     * cost for economic room is 20.
     *
     * @param roomTypes room type
     * @param prices    array of cost for a room in a given standard
     */
    private static void bookRooms(Hotel hotel, RoomType[] roomTypes, Integer... prices) {
        for (int i = 0; i < Math.min(roomTypes.length, prices.length); i++) {
            hotel.bookARoom(roomTypes[i], prices[i]);
        }
    }

    /**
     * Utility method for specifying {@link  RoomType} arrays
     *
     * @param roomTypes an array of room types
     * @return given room types as array type
     */
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

    private static Stream<Arguments> afterBookingMoreThenOneRoomNumberAndValueOfBookedRoomsShouldMatch() {
        return Stream.of(
                Arguments.of(
                        new Integer[]{2, 0}, // available rooms
                        new TestPair(roomTypes(ECONOMY, ECONOMY), 30, 44), // booking data
                        new TestPair(roomTypes(ECONOMY), 0),  // asserting availability
                        new TestPair(roomTypes(ECONOMY), 2), // asserting number of booked rooms
                        new TestPair(roomTypes(ECONOMY), 74)  // asserting value of booked rooms
                ),
                Arguments.of(
                        new Integer[]{2, 3},
                        new TestPair(roomTypes(ECONOMY, PREMIUM, PREMIUM), 30, 100, 321),
                        new TestPair(roomTypes(ECONOMY, PREMIUM), 1, 1),
                        new TestPair(roomTypes(ECONOMY, PREMIUM), 1, 2),
                        new TestPair(roomTypes(ECONOMY, PREMIUM), 30, 421)
                )
        );
    }

    @Test
    void shouldSetRoomAvailabilityForAHotel() {
        Hotel hotel = createHotelWithAvailableRooms(1, 3);

        assertAvailabilityOfRooms(hotel, roomTypes(ECONOMY, PREMIUM), 1, 3);
    }

    @Test
    void byDefaultHotelHasNoRoomsOfAnyType() {
        Hotel hotel = createHotelWithAvailableRooms(0, 0);

        assertAvailabilityOfRooms(hotel, roomTypes(ECONOMY, PREMIUM), 0, 0);
    }

    @Test
    void clientCanBookARoomOfAGivenType() {
        Hotel hotel = createHotelWithAvailableRooms(1, 2);
        bookRooms(hotel, roomTypes(PREMIUM, ECONOMY), 30, 44);

        assertAvailabilityOfRooms(hotel, roomTypes(ECONOMY, PREMIUM), 0, 1);
        assertNumberOfBookedRooms(hotel, roomTypes(ECONOMY, PREMIUM), 1, 1);
        assertValueOfBookedRooms(hotel, roomTypes(ECONOMY, PREMIUM), 44, 30);
    }

    @Test
    void clientCannotBookAnEconomyRoomPayingTooMuch() {
        Hotel hotel = createHotelWithAvailableRooms(1, 0);
        bookRooms(hotel, roomTypes(ECONOMY), ECONOMY_THRESHOLD);

        assertAvailabilityOfRooms(hotel, roomTypes(ECONOMY), 1);
    }

    @ParameterizedTest
    @MethodSource
    void afterBookingMoreThenOneRoomNumberAndValueOfBookedRoomsShouldMatch(
            Integer[] availableRooms,
            TestPair bookingData,
            TestPair assertAvailabilityData,
            TestPair assertNumberOfBookedRoomsData,
            TestPair assertValueOfBookedRoomsData) {
        Hotel hotel = createHotelWithAvailableRooms(availableRooms[0], availableRooms[1]);
        bookRooms(hotel, bookingData.roomTypes, bookingData.values());

        assertAvailabilityOfRooms(hotel, assertAvailabilityData.roomTypes(), assertAvailabilityData.value());
        assertNumberOfBookedRooms(hotel, assertNumberOfBookedRoomsData.roomTypes(), assertNumberOfBookedRoomsData.value());
        assertValueOfBookedRooms(hotel, assertValueOfBookedRoomsData.roomTypes(), assertValueOfBookedRoomsData.value());
    }

    @Test
    void itIsNotPossibleToBookMoreThanAvailableNumberOfRooms() {
        Hotel hotel = createHotelWithAvailableRooms(1, 0);
        bookRooms(hotel, roomTypes(ECONOMY, ECONOMY), 26, 44);

        assertAvailabilityOfRooms(hotel, roomTypes(ECONOMY), 0);
        assertNumberOfBookedRooms(hotel, roomTypes(ECONOMY), 1);
        assertValueOfBookedRooms(hotel, roomTypes(ECONOMY), 26);
    }

    record TestPair(RoomType[] roomTypes, Integer... values) {

        Integer value() {
            return values[0];
        }
    }

}
