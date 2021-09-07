package ru.volod878.exchange_rate.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.volod878.exchange_rate.util.FileLoader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { GiphyClientIntegrationTest.WireMockConfig.class })
@DisplayName("Integration-level testing for GiphyClientIntegrationTest")
class GiphyClientIntegrationTest {

    @Autowired
    private WireMockServer mockService;

    @Autowired
    private GiphyClient client;

    @Value("${giphy.gifs.random}")
    private String endpoint;

    private final String apiKey = "correct-api_key";
    private final String tag = "ball";

    @BeforeEach
    public void init() throws IOException {
        mockService.stubFor(WireMock.get(WireMock.urlEqualTo(endpoint + "?api_key=" + apiKey
                        + "&tag=" + tag))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(FileLoader.read("classpath:gif.json"))
                )
        );
    }

    @Test
    public void shouldReturnGifDataCorrectly() {
        assertTrue(client.getRandomGifByTag(apiKey, tag).containsKey("data"));
    }

    @TestConfiguration
    static class WireMockConfig {

        @Autowired
        private WireMockServer wireMockServer;

        @Bean(initMethod = "start", destroyMethod = "stop")
        protected WireMockServer mockService() {
            return new WireMockServer(4444);
        }

    }
}