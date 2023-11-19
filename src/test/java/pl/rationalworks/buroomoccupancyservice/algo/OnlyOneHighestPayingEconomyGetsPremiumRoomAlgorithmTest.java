package pl.rationalworks.buroomoccupancyservice.algo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import pl.rationalworks.buroomoccupancyservice.DataLoader;
import pl.rationalworks.buroomoccupancyservice.model.Hotel;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;
import pl.rationalworks.buroomoccupancyservice.properties.HotelProperties;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.ECONOMY;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.PREMIUM;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@EnabledIf(expression = "#{environment['hotel.algo'] == 'onlyOneHighestPayingEconomyGetsPremium'}", loadContext = true)
public class OnlyOneHighestPayingEconomyGetsPremiumRoomAlgorithmTest {

    @Autowired
    private HotelProperties hotelProperties;
    @Autowired
    private OnlyOneHighestPayingEconomyGetsPremiumRoomAlgorithm algorithm;
    @Autowired
    private DataLoader dataLoader;
    private ClientPrices clientPrices;

    private static Stream<Arguments> numberOfRoomsBookedShouldMatchWhenBookingForMultipleClients() {
        return Stream.of(
                Arguments.of(3, 3, 3, 3),
                Arguments.of(5, 7, 4, 6),
                Arguments.of(7, 2, 4, 2),
                Arguments.of(1, 10, 1, 7),
                // additional tests
                Arguments.of(0, 10, 0, 7),
                Arguments.of(6, 0, 4, 0),
                Arguments.of(0, 0, 0, 0)
        );
    }

    private static Stream<Arguments> totalPriceOfRoomsBookedShouldMatchWhenBookingForMultipleClients() {
        return Stream.of(
                Arguments.of(3, 3, 167, 738),
                Arguments.of(5, 7, 189, 1054),
                Arguments.of(7, 2, 189, 583),
                Arguments.of(1, 10, 45, 1153),
                // additional tests
                Arguments.of(0, 10, 0, 1153),
                Arguments.of(6, 0, 189, 0),
                Arguments.of(0, 0, 0, 0)
        );
    }

    private Hotel setupHotel(int freeEconomyRooms, int freePremiumRooms) {
        return new Hotel(freeEconomyRooms, freePremiumRooms, hotelProperties.getEconomyThreshold());
    }

    @BeforeAll
    void beforeAll() throws IOException {
        clientPrices = dataLoader.loadData();
    }

    @ParameterizedTest
    @MethodSource
    void numberOfRoomsBookedShouldMatchWhenBookingForMultipleClients(int freeEconomyRooms, int freePremiumRooms,
                                                                     int bookedEconomyRooms, int bookedPremiumRooms) {
        Hotel hotel = setupHotel(freeEconomyRooms, freePremiumRooms);

        algorithm.bookRooms(hotel, clientPrices);

        assertEquals(bookedEconomyRooms, hotel.getNumberOfBookedRooms(ECONOMY));
        assertEquals(bookedPremiumRooms, hotel.getNumberOfBookedRooms(PREMIUM));
    }

    @ParameterizedTest
    @MethodSource
    void totalPriceOfRoomsBookedShouldMatchWhenBookingForMultipleClients(int freeEconomyRooms, int freePremiumRooms,
                                                                         int totalValueOfEconomyRooms, int totalValueOfPremiumRooms) {
        Hotel hotel = setupHotel(freeEconomyRooms, freePremiumRooms);

        algorithm.bookRooms(hotel, clientPrices);

        assertEquals(totalValueOfEconomyRooms, hotel.getValueOfBookedRooms(ECONOMY));
        assertEquals(totalValueOfPremiumRooms, hotel.getValueOfBookedRooms(PREMIUM));
    }

}
