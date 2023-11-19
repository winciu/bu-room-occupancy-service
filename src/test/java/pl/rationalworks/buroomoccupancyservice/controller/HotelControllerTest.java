package pl.rationalworks.buroomoccupancyservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.rationalworks.buroomoccupancyservice.HotelService;
import pl.rationalworks.buroomoccupancyservice.model.Hotel;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.ECONOMY;
import static pl.rationalworks.buroomoccupancyservice.model.RoomType.PREMIUM;

@WebMvcTest(HotelController.class)
public class HotelControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HotelService hotelService;

    @Test
    void shouldBookCorrectNumberOfRoomsOfParticularType() throws Exception {
        ClientPrices clientPrices = new ClientPrices(new int[]{23, 45, 155, 374, 22, 99, 100, 101, 115, 209});
        when(hotelService.bookHotelRoomsForClients(any(ClientPrices.class)))
                .thenReturn(new Hotel(3, 7, 100));

        mvc.perform(post("/hotel/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientPrices)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("rooms", hasSize(2)))
                .andExpect(jsonPath("rooms[*].roomType", containsInAnyOrder(ECONOMY.name(), PREMIUM.name())))
                .andExpect(jsonPath("rooms[*].available", hasItems(3, 7)));
    }


}
