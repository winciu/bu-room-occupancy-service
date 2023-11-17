package pl.rationalworks.buroomoccupancyservice;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.rationalworks.buroomoccupancyservice.model.Hotel;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.ECONOMY;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.PREMIUM;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingServiceTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private DataLoader dataLoader;
    private ClientPrices clientPrices;

    @BeforeAll
    void beforeAll() throws IOException {
        clientPrices = dataLoader.loadData();
    }

    @Test
    void shouldSetupAHotelInstanceProperly() {
        Hotel hotel = bookingService.setupHotel(2, 12);
        assertEquals(2, hotel.getAvailableRooms(ECONOMY));
        assertEquals(12, hotel.getAvailableRooms(PREMIUM));
    }

    private static Stream<Arguments> numberOfRoomsBookedShouldMatchWhenBookingForMultipleClients() {
        return Stream.of(
                Arguments.of(3, 3, 3, 3),
                Arguments.of(5, 7, 3, 7),
                Arguments.of(7, 2, 4, 2),
                Arguments.of(1, 10, 0, 10)
        );
    }

    private static Stream<Arguments> totalPriceOfRoomsBookedShouldMatchWhenBookingForMultipleClients() {
        return Stream.of(
                Arguments.of(3, 3, 167, 738),
                Arguments.of(5, 7, 90, 1153),
                Arguments.of(7, 2, 189, 583),
                Arguments.of(1, 10, 0, 1243)
        );
    }

    @ParameterizedTest
    @MethodSource
    void numberOfRoomsBookedShouldMatchWhenBookingForMultipleClients(int freeEconomyRooms, int freePremiumRooms,
                                                                     int bookedEconomyRooms, int bookedPremiumRooms) {
        Hotel hotel = bookingService.setupHotel(freeEconomyRooms, freePremiumRooms);

        bookingService.bookHotelRoomsForClients(hotel, clientPrices);

        assertEquals(bookedEconomyRooms, hotel.getNumberOfBookedRooms(ECONOMY));
        assertEquals(bookedPremiumRooms, hotel.getNumberOfBookedRooms(PREMIUM));
    }

    @ParameterizedTest
    @MethodSource
    void totalPriceOfRoomsBookedShouldMatchWhenBookingForMultipleClients(int freeEconomyRooms, int freePremiumRooms,
                                                                         int totalValueOfEconomyRooms, int totalValueOfPremiumRooms) {
        Hotel hotel = bookingService.setupHotel(freeEconomyRooms, freePremiumRooms);

        bookingService.bookHotelRoomsForClients(hotel, clientPrices);

        assertEquals(totalValueOfEconomyRooms, hotel.getValueOfBookedRooms(ECONOMY));
        assertEquals(totalValueOfPremiumRooms, hotel.getValueOfBookedRooms(PREMIUM));
    }


}
