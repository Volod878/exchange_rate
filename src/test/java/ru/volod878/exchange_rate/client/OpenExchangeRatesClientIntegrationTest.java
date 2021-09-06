package ru.volod878.exchange_rate.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.volod878.exchange_rate.configuration.WireMockConfig;
import ru.volod878.exchange_rate.model.ExchangeRateBean;
import ru.volod878.exchange_rate.util.FileLoader;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WireMockConfig.class })
@DisplayName("Integration-level testing for OpenExchangeRatesClientIntegrationTest")
class OpenExchangeRatesClientIntegrationTest {

    @Autowired
    private WireMockServer mockService;

    @Autowired
    private OpenExchangeRatesClient client;

    @Value("${open_exchange_rate.currencies}")
    private String endpointCurrencies;

    @Value("${open_exchange_rate.latest}")
    private String endpointLatest;

    @Value("${open_exchange_rate.historical}/yesterday.json")
    String endpointHistorical;

    private final String appID = "correct-app_id";

    @Test
    public void shouldReturnExchangeRateCorrectly() {
        setupMockResponse(endpointLatest + "?app_id=" + appID, "exchangeRate.json");

        Map<String, Double> rates = new TreeMap<>();
        rates.put("AED", 3.6732);
        rates.put("AFN", 86.890566);
        rates.put("ALL", 102.576868);
        rates.put("AMD", 493.432427);

        ExchangeRateBean exchangeRateBean = new ExchangeRateBean();
        exchangeRateBean.setDisclaimer("Usage subject to terms: https://openexchangerates.org/terms");
        exchangeRateBean.setLicense("https://openexchangerates.org/license");
        exchangeRateBean.setTimestamp(1630929600);
        exchangeRateBean.setBase("USD");
        exchangeRateBean.setRates(rates);

        assertThat(client.getExchangeRate(appID), is(exchangeRateBean));
    }

    @Test
    public void shouldReturnYesterdayExchangeRateCorrectly() {
        setupMockResponse(endpointHistorical + "?app_id=" + appID, "yesterdayExchangeRate.json");

        Map<String, Double> rates = new TreeMap<>();
        rates.put("AED", 3.673041);
        rates.put("AFN", 86.878888);
        rates.put("ALL", 102.620677);
        rates.put("AMD", 493.411507);

        ExchangeRateBean exchangeRateBean = new ExchangeRateBean();
        exchangeRateBean.setDisclaimer("Usage subject to terms: https://openexchangerates.org/terms");
        exchangeRateBean.setLicense("https://openexchangerates.org/license");
        exchangeRateBean.setTimestamp(1630886399);
        exchangeRateBean.setBase("USD");
        exchangeRateBean.setRates(rates);

        assertThat(client.getYesterdayExchangeRate("yesterday", appID), is(exchangeRateBean));
    }

    @Test
    public void whenGetAllCodes_thenTheCorrectCodesShouldBeReturned() {
        setupMockResponse(endpointCurrencies, "allCodesExchangeRate.json");

        Map<String, String> expectedCodes = new TreeMap<>();
        expectedCodes.put("AED", "United Arab Emirates Dirham");
        expectedCodes.put("AFN", "Afghan Afghani");
        expectedCodes.put("ALL", "Albanian Lek");
        expectedCodes.put("AMD", "Armenian Dram");

        assertEquals(client.getAllCodesExchangeRate(), expectedCodes);
    }

    private void setupMockResponse(String endpoint, String classpath) {
        try {
            mockService.stubFor(WireMock.get(WireMock.urlEqualTo(endpoint))
                    .willReturn(WireMock.aResponse()
                            .withStatus(HttpStatus.OK.value())
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .withBody(FileLoader.read("classpath:" + classpath))
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}