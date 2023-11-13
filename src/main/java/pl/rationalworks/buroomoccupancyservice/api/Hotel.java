package pl.rationalworks.buroomoccupancyservice.api;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class Hotel {
    private Map<RoomType, Integer> availableRooms = new HashMap<>();

    public Integer setAvailableRooms(RoomType roomType, Integer amount){
        return availableRooms.put(roomType, amount);
    }

    public Integer getAvailableRooms(RoomType roomType) {
        return availableRooms.getOrDefault(roomType, 0);
    }
}
