package pl.rationalworks.buroomoccupancyservice;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.rationalworks.buroomoccupancyservice.model.Hotel;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;

import java.io.IOException;

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

    @Test
    void numberOfRoomsBookedShouldMatchWhenBookingForMultipleClients() {
        Hotel hotel = bookingService.setupHotel(3, 3);

        bookingService.bookHotelRoomsForClients(hotel, clientPrices);

        assertEquals(3, hotel.getNumberOfBookedRooms(ECONOMY));
        assertEquals(3, hotel.getNumberOfBookedRooms(PREMIUM));
    }

    @Test
    void totalPriceOfRoomsBookedShouldMatchWhenBookingForMultipleClients() {
        Hotel hotel = bookingService.setupHotel(3, 3);

        bookingService.bookHotelRoomsForClients(hotel, clientPrices);

        assertEquals(167, hotel.getValueOfBookedRooms(ECONOMY));
        assertEquals(738, hotel.getValueOfBookedRooms(PREMIUM));
    }


}
