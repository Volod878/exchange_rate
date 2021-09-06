package ru.volod878.exchange_rate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.volod878.exchange_rate.model.ExchangeRateBean;
import ru.volod878.exchange_rate.service.ExchangeRateService;

import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayName("Unit-level testing for ExchangeRateControllerTest")
public class ExchangeRateControllerTest {

    private ExchangeRateController controller;

    @Mock
    private ExchangeRateService service;

    private final String appID = "correct app_id";
    private final Map<String, Double> expectedRates = new TreeMap<>();
    private final ExchangeRateBean expectedExchangeRateBean = new ExchangeRateBean();

    @BeforeEach
    public void init() {
        openMocks(this);

        controller = new ExchangeRateController(service);

        expectedRates.put("USD", 0.01373014212976308);
        expectedRates.put("EUR", 0.01155053698723171);
        expectedRates.put("RUB", 1d);
        expectedRates.put("GYD", 2.8717608294917083);
        expectedRates.put("BOB", 0.09518887856290667);

        expectedExchangeRateBean.setBase("RUB");
        expectedExchangeRateBean.setRates(expectedRates);
    }

    @Test
    public void shouldReturnExchangeRateBean() {
        given(service.getExchangeRate(appID)).willReturn(expectedExchangeRateBean);

        ExchangeRateBean actualExchangeRateBean = controller.getExchangeRate(appID);

        assertEquals(expectedExchangeRateBean.getBase(), actualExchangeRateBean.getBase());
        assertEquals(expectedExchangeRateBean.getRates(), actualExchangeRateBean.getRates());


        given(service.getYesterdayExchangeRate(appID)).willReturn(expectedExchangeRateBean);

        actualExchangeRateBean = controller.getYesterdayExchangeRate(appID);

        assertEquals(expectedExchangeRateBean.getBase(), actualExchangeRateBean.getBase());
        assertEquals(expectedExchangeRateBean.getRates(), actualExchangeRateBean.getRates());
    }

    @Test
    public void shouldReturnCorrectCodesExchangeRate() {
        Map<String, String> allCodesExchangeRate = new TreeMap<>();
        allCodesExchangeRate.put("AED", "United Arab Emirates Dirham");
        allCodesExchangeRate.put("AFN", "Afghan Afghani");
        allCodesExchangeRate.put("ALL", "Albanian Lek");
        allCodesExchangeRate.put("AMD", "Armenian Dram");

        given(service.getAllCodesExchangeRate()).willReturn(allCodesExchangeRate);

        assertThat(controller.getAllCodesExchangeRate(), is(allCodesExchangeRate));
    }

    @Test
    public void shouldReturnCorrectURL() {
        String currency = "EUR";
        given(service.isCurrencyRateHigherThanBaseRate(currency, appID)).willReturn(true);

        String apiKey = "correct api_key";
        assertEquals(controller.getGifByStatus(currency, appID, apiKey).getUrl(),
                "/gif/rich?api_key=" + apiKey);

        given(service.isCurrencyRateHigherThanBaseRate(currency, appID)).willReturn(false);

        assertEquals(controller.getGifByStatus(currency, appID, apiKey).getUrl(),
                "/gif/broke?api_key=" + apiKey);
    }
}