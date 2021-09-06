package ru.volod878.exchange_rate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import ru.volod878.exchange_rate.client.OpenExchangeRatesClient;
import ru.volod878.exchange_rate.model.ExchangeRateBean;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.mockito.BDDMockito.given;

@DisplayName("Unit-level testing for ExchangeRateServiceTest")
public class ExchangeRateServiceTest {

    private final String base = "RUB";

    @Value("${server.port}")
    private String port = "8080";

    @Mock
    private OpenExchangeRatesClient client;

    private ExchangeRateService exchangeRateService;

    private final String appID = "correct app_id";
    private final String currencyWentUp = "EUR";
    private final String currencyIsPreserved = "USD";
    private final String currencyFell = "GYD";
    private final Map<String, Double> todayRates = new TreeMap<>();
    private final Map<String, Double> yesterdayRates = new TreeMap<>();
    private final Map<String, String> allCodesExchangeRate = new TreeMap<>();

    @BeforeEach
    public void init() throws NoSuchFieldException, IllegalAccessException {
        openMocks(this);

        exchangeRateService = new ExchangeRateService(client);
        Class<? extends ExchangeRateService> serviceClass = exchangeRateService.getClass();
        Field baseField = serviceClass.getDeclaredField("base");
        baseField.setAccessible(true);
        baseField.set(exchangeRateService, base);

        Field portField = serviceClass.getDeclaredField("port");
        portField.setAccessible(true);
        portField.set(exchangeRateService, port);

        todayRates.put(currencyWentUp, 0.8);
        todayRates.put(currencyIsPreserved, 1d);
        todayRates.put(base, 72.832);
        todayRates.put(currencyFell, 209.0);

        yesterdayRates.put(currencyWentUp, 0.841);
        yesterdayRates.put(currencyIsPreserved, 1d);
        yesterdayRates.put(base, 72.832);
        yesterdayRates.put(currencyFell, 199.0);

        allCodesExchangeRate.put(currencyWentUp, "");
        allCodesExchangeRate.put(currencyIsPreserved, "");
        allCodesExchangeRate.put(base, "");
        allCodesExchangeRate.put(currencyFell, "");
    }

    @Test
    public void shouldChangeBaseCorrectly() {
        Map<String, Double> rates = new TreeMap<>();
        rates.put("USD", 1d);
        rates.put("EUR", 0.841254);
        rates.put("RUB", 72.832458);
        rates.put("GYD", 209.1574);
        rates.put("BOB", 6.93284);

        ExchangeRateBean exchangeRateBean = new ExchangeRateBean();
        exchangeRateBean.setBase("USD");
        exchangeRateBean.setRates(rates);

        given(client.getExchangeRate(appID)).willReturn(exchangeRateBean);

        Map<String, Double> actualRates = new TreeMap<>();
        actualRates.put("USD", 0.01373014212976308);
        actualRates.put("EUR", 0.01155053698723171);
        actualRates.put("RUB", 1d);
        actualRates.put("GYD", 2.8717608294917083);
        actualRates.put("BOB", 0.09518887856290667);

        ExchangeRateBean actualExchangeRateBean = new ExchangeRateBean();
        actualExchangeRateBean.setBase(base);
        actualExchangeRateBean.setRates(actualRates);

        ExchangeRateBean expectedExchangeRateBean = exchangeRateService.getExchangeRate(appID);

        assertEquals(expectedExchangeRateBean, actualExchangeRateBean);
    }

    @Test
    public void shouldReturn_True_IfCurrencyRateIsHigherThanBase() {
        given(client.getAllCodesExchangeRate()).willReturn(allCodesExchangeRate);

        ExchangeRateBean todayExchangeRate = new ExchangeRateBean();
        todayExchangeRate.setRates(todayRates);
        given(client.getExchangeRate(appID)).willReturn(todayExchangeRate);

        ExchangeRateBean yesterdayExchangeRate = new ExchangeRateBean();
        yesterdayExchangeRate.setRates(yesterdayRates);
        String yesterday = LocalDate.now(ZoneId.of("UTC")).minusDays(1).toString();
        given(client.getYesterdayExchangeRate(yesterday, appID)).willReturn(yesterdayExchangeRate);

        assertTrue(exchangeRateService.isCurrencyRateHigherThanBaseRate(currencyWentUp, appID));
    }

    @Test
    public void shouldReturn_False_IfCurrencyRateIsNotHigherThanBase() {
        given(client.getAllCodesExchangeRate()).willReturn(allCodesExchangeRate);

        ExchangeRateBean todayExchangeRate = new ExchangeRateBean();
        todayExchangeRate.setRates(todayRates);
        given(client.getExchangeRate(appID)).willReturn(todayExchangeRate);

        ExchangeRateBean yesterdayExchangeRate = new ExchangeRateBean();
        yesterdayExchangeRate.setRates(yesterdayRates);
        String yesterday = LocalDate.now(ZoneId.of("UTC")).minusDays(1).toString();
        given(client.getYesterdayExchangeRate(yesterday, appID)).willReturn(yesterdayExchangeRate);

        assertFalse(exchangeRateService.isCurrencyRateHigherThanBaseRate(currencyFell, appID));
        assertFalse(exchangeRateService.isCurrencyRateHigherThanBaseRate(currencyIsPreserved, appID));
    }
}
