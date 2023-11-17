package pl.rationalworks.buroomoccupancyservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.rationalworks.buroomoccupancyservice.model.dto.ClientPrices;

import java.io.IOException;


@SpringBootTest
@ActiveProfiles("test")
public class DataLoaderTest {

    @Autowired
    private DataLoader dataLoader;

    @Test
    public void shouldLoadInputDataProperly() throws IOException {
        ClientPrices clientPrices = dataLoader.loadData();
        Assertions.assertEquals(10, clientPrices.prices().length);
    }
}
