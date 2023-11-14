package pl.rationalworks.buroomoccupancyservice.api;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class Hotel {
    private final Map<RoomType, Integer> availableRooms = new HashMap<>();

    /**
     * A map storing an information about already booked rooms of a given type
     * and a total price (after summing up each booking price in a given list).
     */
    private final Map<RoomType, List<Integer>> bookedRooms = new HashMap<>();

    public Integer setAvailableRooms(RoomType roomType, Integer amount){
        return availableRooms.put(roomType, amount);
    }

    public Integer getAvailableRooms(RoomType roomType) {
        return availableRooms.getOrDefault(roomType, 0);
    }

    public Integer getNumberOfBookedRooms(RoomType roomType) {
        return bookedRooms.get(roomType).size();
    }

    public Integer getValueOfBookedRooms(RoomType roomType) {
        return bookedRooms.get(roomType).stream().mapToInt(Integer::intValue).sum();
    }

    public void bookARoom(RoomType roomType, Integer price) {
        availableRooms.computeIfPresent(roomType, (rt, amountOfAvailableRooms) -> {
            bookedRooms.putIfAbsent(rt, new ArrayList<>());
            //if  we still have available rooms
            boolean tooMuchForEconomy = roomType.equals(RoomType.ECONOMY) && price >= 100;
            if (amountOfAvailableRooms > 0) {
                bookedRooms.computeIfPresent(roomType, (rType, prices) -> {
                    if (!tooMuchForEconomy) {
                        prices.add(price);
                    }
                    return prices;
                });
                if (tooMuchForEconomy) {
                    return amountOfAvailableRooms;
                }
                return amountOfAvailableRooms - 1;
            }
            return amountOfAvailableRooms;
        });
    }
}
