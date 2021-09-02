package ru.volod878.exchange_rate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.volod878.exchange_rate.client.OpenExchangeRatesClient;
import ru.volod878.exchange_rate.model.ExchangeRateBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class ExchangeRateService {

    @Value("${exchange_rate.base}")
    private String base;

    private final OpenExchangeRatesClient client;

    @Autowired
    public ExchangeRateService(OpenExchangeRatesClient client) {
        this.client = client;
    }

    public ExchangeRateBean getExchangeRate(String appID) {
        return client.getExchangeRate(appID);
    }

    public ExchangeRateBean getYesterdayExchangeRate(String appID) {
        String yesterday = LocalDate.now(ZoneId.of("UTC")).minusDays(1).toString();

        return client.getYesterdayExchangeRate(yesterday, appID);
    }

    public boolean isCurrencyRateHigherThanBaseRate(String currency, String appID) {
        ExchangeRateBean exchangeRateToday = getExchangeRate(appID);
        ExchangeRateBean exchangeRateYesterday = getYesterdayExchangeRate(appID);

        RoundingMode halfUp = RoundingMode.HALF_UP;

        BigDecimal rateToday = BigDecimal.ONE
                .divide(BigDecimal.valueOf(exchangeRateToday.getRates().get(base)), 10, halfUp);
        Double currencyRateToday = exchangeRateToday.getRates().get(currency);
        currencyRateToday = rateToday.multiply(BigDecimal.valueOf(currencyRateToday)).doubleValue();

        BigDecimal rateYesterday = BigDecimal.ONE
                .divide(BigDecimal.valueOf(exchangeRateYesterday.getRates().get(base)), 10, halfUp);
        Double currencyRateYesterday = exchangeRateYesterday.getRates().get(currency);
        currencyRateYesterday = rateYesterday.multiply(BigDecimal.valueOf(currencyRateYesterday)).doubleValue();

        return currencyRateToday < currencyRateYesterday;
    }
}
