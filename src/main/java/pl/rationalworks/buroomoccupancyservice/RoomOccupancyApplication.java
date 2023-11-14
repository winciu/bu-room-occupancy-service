package pl.rationalworks.buroomoccupancyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan("pl.rationalworks.buroomoccupancyservice.properties")
public class RoomOccupancyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomOccupancyApplication.class, args);
	}

}
