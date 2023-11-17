package pl.rationalworks.buroomoccupancyservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "data-loader")
@Getter
@Setter
public class DataLoaderProperties {
    private String mode;

    @Value("${data-loader.source.local}")
    private String sourceLocal;

    @Value("${data-loader.source.remote}")
    private URI sourceRemote;
}
