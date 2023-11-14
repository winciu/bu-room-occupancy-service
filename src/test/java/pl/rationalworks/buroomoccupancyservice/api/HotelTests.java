package pl.rationalworks.buroomoccupancyservice.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.rationalworks.buroomoccupancyservice.api.RoomType.ECONOMY;
import static pl.rationalworks.buroomoccupancyservice.api.RoomType.PREMIUM;

public class HotelTests {

    /**
     * Utility method for creating a {@link Hotel} instance for a test. When using this method you need to obey a convention
     * by specifying its input arguments. Both input parameters of this method are arrays where items with the same
     * index forms a logical relation. <br/><br/>
     * For instance, when {@code roomTypes = [ECONOMIC, PREMIUM]}  and {@code availableRooms = [2, 5]} it means that
     * there are 2 available rooms in an economic standard.
     *
     * @param roomTypes      room type
     * @param availableRooms number of available rooms for a given room type (an index should be relevant to the index
     *                       of roomTypes array.
     * @return a {@link Hotel} instance with initialized data for available rooms
     */
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

    private static Stream<Arguments> afterBookingMoreThenOneRoomNumberAndValueOfBookedRoomsShouldMatch() {
        return Stream.of(
                Arguments.of(
                        new TestPair(roomTypes(ECONOMY), 2), // available rooms
                        new TestPair(roomTypes(ECONOMY, ECONOMY), 30, 44), // booking data
                        new TestPair(roomTypes(ECONOMY), 0),  // asserting availability
                        new TestPair(roomTypes(ECONOMY), 2), // asserting number of booked rooms
                        new TestPair(roomTypes(ECONOMY), 74)  // asserting value of booked rooms
                ),
                Arguments.of(
                        new TestPair(roomTypes(ECONOMY, PREMIUM), 2, 3),
                        new TestPair(roomTypes(ECONOMY, PREMIUM, PREMIUM), 30, 100, 321),
                        new TestPair(roomTypes(ECONOMY, PREMIUM), 1, 1),
                        new TestPair(roomTypes(ECONOMY, PREMIUM), 1, 2),
                        new TestPair(roomTypes(ECONOMY, PREMIUM), 30, 421)
                )
        );
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
    void clientCannotBookAnEconomyRoomPayingTooMuch() {
        Hotel hotel = createHotelWithAvailableRooms(roomTypes(ECONOMY), 1);
        final int tooMuchPriceForEconomyRoom = 100;
        bookRooms(hotel, roomTypes(ECONOMY), tooMuchPriceForEconomyRoom);

        assertAvailabilityOfRooms(hotel, roomTypes(ECONOMY), 1);
    }

    @ParameterizedTest
    @MethodSource
    void afterBookingMoreThenOneRoomNumberAndValueOfBookedRoomsShouldMatch(
            TestPair availableRooms,
            TestPair bookingData,
            TestPair assertAvailabilityData,
            TestPair assertNumberOfBookedRoomsData,
            TestPair assertValueOfBookedRoomsData) {
        Hotel hotel = createHotelWithAvailableRooms(availableRooms.roomTypes, availableRooms.values);
        bookRooms(hotel, bookingData.roomTypes, bookingData.values());

        assertAvailabilityOfRooms(hotel, assertAvailabilityData.roomTypes(), assertAvailabilityData.value());
        assertNumberOfBookedRooms(hotel, assertNumberOfBookedRoomsData.roomTypes(), assertNumberOfBookedRoomsData.value());
        assertValueOfBookedRooms(hotel, assertValueOfBookedRoomsData.roomTypes(), assertValueOfBookedRoomsData.value());
    }

    @Test
    void itIsNotPossibleToBookMoreThanAvailableNumberOfRooms() {
        Hotel hotel = createHotelWithAvailableRooms(roomTypes(ECONOMY), 1);
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
