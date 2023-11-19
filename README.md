# Hotel room reservation service

This is a simple Spring Boot service for booking hotel rooms based on the price a hotel guest can offer for a room.

# Build

In order to build the service and run tests you need to download the source code and save it in the folder of your choice. Then go to that folder and run the following command to build the jar file.

    mvn clean install

As a prerequisite you need to have `Maven` and `Java 17` already installed.

# Run

Once the service is build you can run it by executing the command below.

    java -jar target\bu-room-occupancy-service-0.0.1-SNAPSHOT.jar

# Usage
### Java API
If you would like to check how the service works just by calling/analyzing Java classes please hava a look at the `HotelService` class as the first step - starting point.
It has two main methods: `setupHotel` and `bookHotelRoomsForClients`. They need to be called exactly in this order. First you need to setup a Hotel instance with the info about room availability and then book rooms by providing client prices.
\
As an output you will get a `Hotel` object instance contatining all the information about rooms availability (after booking), number of booked rooms (of each type) and the total price for a room of a given type.
### REST API
Assuming the service is up and running, just for the sake of initial testing, you can execute the command below to get the current state of a hotel object.
This can be also a good way to figure out how the hotel related json looks like

    $ curl http://localhost:8080/hotel

Furthermore, you can use the `/hotel/setup` endpoint to initialize the hotel with available number of rooms (economy and premium)

For example, to set 3 economy rooms and 5 premium rooms as available do the following request

    $ curl -X PUT --data '[{"roomType":"PREMIUM","available":5},{"roomType":"ECONOMY","available":3}]' -H "Content-Type: application/json" http://localhost:8085/hotel/setup

or alternatively use a provided json snippet with payload data:

    $ curl -X PUT -d @src/test/resources/hotelSetup.json -H "Content-Type: application/json" http://localhost:8085/hotel/setup

In order to invoke booking algorithm and verify room allocation and total price/cost based on room type, make the `POST` request like below:

    $ curl -X POST -d "[23, 45, 155, 374, 22, 99, 100, 101]" -H "Content-Type: application/json" http://localhost:8085/hotel/book

or alternatively used the provided data for client prices from the json file

    $ curl -X POST -d @src/test/resources/smarthost_hotel_guests.json -H "Content-Type: application/json" http://localhost:8085/hotel/book

In a response you should get a JSON like this:

```
{
  "rooms": [
    {
      "roomType": "ECONOMY",
      "available": 3,
      "booked": 4,
      "totalPrice": 189
    },
    {
      "roomType": "PREMIUM",
      "available": 0,
      "booked": 2,
      "totalPrice": 583
    }
  ]
}
```

**NOTE!**
Please note, that before booking rooms (`/hote/book` endpoint) you need to setup the hotel with available rooms by making `/hotel/setup` request.
By default, hotel has no available rooms of any category, that is `ECONOMY` and `PREMIUM`.

---
### Remarks about the logic used and some thoughts on development decisions

1. Although the domain model itself is pretty small I have decided not to make it anemic (more on this [here](https://martinfowler.com/bliki/AnemicDomainModel.html])).
This is why a `Hotel` class has `bookARoom(RoomType roomType, Integer price)` method. This method is not an actual booking algorithm as described in a task description.
It just allows to do a simple booking utilizing a piece of logic that is common to all booking operations and rather reflects the internal hotel's constraint on booking. It could be applied to any price a client can offer for a room.
The logic in this method is more related to checking availability of rooms or updating the number of already booked rooms.



2. There are two booking algorithms developed in this service. The first one I started working on was `MaximizePremiumRoomsAlgorithm`.
Why there are 2 algorithms? This is because of my understanding of the task description (the alogrithm logic). I have started the development based on the description, but after
I analyse the attached test cases it turned out that one of the test case (the last one - 4th) works differently then described earlier.
\
Let me here quote some fragment from the task description and the aforementioned test case.
Snippet from the task description:
>Our hotels
want their customers to be satisfied: they will not book a customer willing to pay over EUR
100 for the night into an Economy room. But they will book lower paying customers into
Premium rooms if these rooms would be empty and all Economy rooms will be filled by low
paying customers. Highest paying customers below EUR 100 will get preference for the
“upgrade”.

Please notice the plural form in the last sentence: "Highest paying customers ..."

Test no. 4
```
Test 4
Free Premium rooms: 10
Free Economy rooms: 1
Usage Premium: 7 (EUR 1153)
Usage Economy: 1 (EUR 45)
```
The expected values in that test together with provided input (guest) prices means that only one customer will be upgraded.
Only this one, which offers the highest price below 100 EUR.
\
So this is why the 2nd algorithm was developed, namely `OnlyOneHighestPayingEconomyGetsPremiumRoomAlgorithm`. This algorithm is currently the default one.
\
In my opinion the 1st algorithm (`MaximizePremiumRoomsAlgorithm`) is better in this case (test #4).
In this algorithm it's not like just the only one guest with the highest price will get the `PREMIUM` room. It's the opposite. Every 'economy' client with the highest (*) price will get the premium room if such rooms are still available.

(*) - highest price in this context means higher than the other economy clients

As you look at Test #4 only 8 rooms are occupied whereas there are 10 rooms available in the hotel and actually we have 10 clients waiting to book a room.
Using `MaximizePremiumRoomsAlgorithm` algorithm all 10 premium rooms will be booked by all the clients (even those with economy prices). That means that in total hotel will have higher income, comparing to the other algorithm where only 8 rooms are booked and 2 are still available although there were guests waiting to book those rooms.
\
I know that in this scenario more premium rooms will be booked by economy clients, but it might mean that Hotel will earn more money in total by not leaving any empty room if this is possible (there are clients wanting to pay for a room).

---
Please notice that the default service port is `8085`. This value is also configurable in the `application.yaml` file or by specifying the corresponding runtime parameter.
