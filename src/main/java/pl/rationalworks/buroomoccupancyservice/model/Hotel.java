package pl.rationalworks.buroomoccupancyservice.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hotel {

    @Getter
    private final Integer economyThresholdValue;
    private final Map<RoomType, Integer> availableRooms = new HashMap<>();

    /**
     * A map storing an information about already booked rooms of a given type
     * and a total price (after summing up each booking price in a given list).
     */
    private final Map<RoomType, List<Integer>> bookedRooms = new HashMap<>();

    public Hotel(int economyRooms, int premiumRooms, Integer economyThresholdValue) {
        availableRooms.put(RoomType.ECONOMY, economyRooms);
        availableRooms.put(RoomType.PREMIUM, premiumRooms);
        this.economyThresholdValue = economyThresholdValue;
    }

    public Integer getAvailableRooms(RoomType roomType) {
        return availableRooms.getOrDefault(roomType, 0);
    }

    public Integer getNumberOfBookedRooms(RoomType roomType) {
        return bookedRooms.getOrDefault(roomType, List.of()).size();
    }

    public Integer getValueOfBookedRooms(RoomType roomType) {
        return bookedRooms.getOrDefault(roomType, List.of()).stream().mapToInt(Integer::intValue).sum();
    }

    public void bookARoom(RoomType roomType, Integer price) {
        availableRooms.computeIfPresent(roomType, (rt, amountOfAvailableRooms) -> {
            bookedRooms.putIfAbsent(rt, new ArrayList<>());
            // clients offering high prices (above threshold) should not get an economy room
            boolean tooMuchForEconomy = roomType.equals(RoomType.ECONOMY) && price >= economyThresholdValue;
            //if we still have available rooms
            if (amountOfAvailableRooms > 0) {
                // book a room and update total price
                bookedRooms.computeIfPresent(roomType, (rType, prices) -> {
                    if (!tooMuchForEconomy) {
                        prices.add(price);
                    }
                    return prices;
                });
                if (tooMuchForEconomy) {
                    return amountOfAvailableRooms; // do nothing (return same value), since we cannot handle such case
                }
                return amountOfAvailableRooms - 1; //decrease # of available rooms if successfully booked
            }
            return amountOfAvailableRooms; // if no available rooms, do nothing
        });
    }
}
