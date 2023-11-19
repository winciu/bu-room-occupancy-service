package pl.rationalworks.buroomoccupancyservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rationalworks.buroomoccupancyservice.HotelService;
import pl.rationalworks.buroomoccupancyservice.model.Hotel;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;
import pl.rationalworks.buroomoccupancyservice.model.dto.HotelDto;
import pl.rationalworks.buroomoccupancyservice.model.dto.RoomDto;

import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.ECONOMY;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.PREMIUM;

@RestController
@RequestMapping("/hotel")
@RequiredArgsConstructor
@Slf4j
public class HotelController {

    private final HotelService hotelService;

    private static HotelDto convertToHotelDto(Hotel hotel) {
        return new HotelDto(
                Set.of(new RoomDto(ECONOMY,
                                hotel.getAvailableRooms(ECONOMY),
                                hotel.getNumberOfBookedRooms(ECONOMY),
                                hotel.getValueOfBookedRooms(ECONOMY)),
                        new RoomDto(PREMIUM,
                                hotel.getAvailableRooms(PREMIUM),
                                hotel.getNumberOfBookedRooms(PREMIUM),
                                hotel.getValueOfBookedRooms(PREMIUM))),
                hotel.getEconomyThresholdValue());
    }

    @GetMapping()
    public ResponseEntity<HotelDto> obtainHotelData() {
        log.info("Retrieving hotel data ...");
        return ResponseEntity.ok(convertToHotelDto(hotelService.getHotel()));
    }

    @PutMapping(value = "/setup", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<HotelDto> setupAvailableRooms(@RequestBody List<RoomDto> rooms) {
        int availableEconomyRooms = rooms.stream()
                .filter(r -> r.roomType().equals(ECONOMY))
                .mapToInt(RoomDto::available)
                .sum();
        int availablePremiumRooms = rooms.stream()
                .filter(r -> r.roomType().equals(PREMIUM))
                .mapToInt(RoomDto::available)
                .sum();

        Hotel hotel = hotelService.setupHotel(availableEconomyRooms, availablePremiumRooms);

        HotelDto hotelDto = convertToHotelDto(hotel);
        return ResponseEntity.ok(hotelDto);
    }

    /**
     * @param clientPrices an instance of {@link ClientPrices} object containing proposed prices
     * @return an instance of a hotel object describing its current state
     */
    @PostMapping("/book")
    public ResponseEntity<HotelDto> bookRooms(@RequestBody ClientPrices clientPrices) {
        Hotel hotel = hotelService.bookHotelRoomsForClients(clientPrices);
        return ResponseEntity.ok(convertToHotelDto(hotel));
    }
}
