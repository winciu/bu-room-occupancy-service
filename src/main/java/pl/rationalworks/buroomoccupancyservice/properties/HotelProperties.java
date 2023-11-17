package pl.rationalworks.buroomoccupancyservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hotel")
@Getter
@Setter
public class HotelProperties {

    @Value("${hotel.economy.threshold}")
    private Integer economyThreshold;
}
