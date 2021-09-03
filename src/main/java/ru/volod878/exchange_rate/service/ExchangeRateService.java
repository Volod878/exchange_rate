package ru.volod878.exchange_rate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.volod878.exchange_rate.client.OpenExchangeRatesClient;
import ru.volod878.exchange_rate.exception_handling.exception.NoSuchExchangeRatesException;
import ru.volod878.exchange_rate.model.ExchangeRateBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

/**
 * Класс-сервис.
 * Обрабатывает данные полученные с клиента и передает в контроллер
 */
@Service
public class ExchangeRateService {

    @Value("${exchange_rate.base}")
    private String base;

    private final OpenExchangeRatesClient client;

    @Autowired
    public ExchangeRateService(OpenExchangeRatesClient client) {
        this.client = client;
    }

    /**
     * Получаем данные по всем курсам валют на сегодняшний день
     * @param appID идентификатор для аутентификации на внешнем сервере курсов валют
     * @return данные по курсам валют сохраненные в объекте ExchangeRateBean
     */
    public ExchangeRateBean getExchangeRate(String appID) {
        return client.getExchangeRate(appID);
    }

    /**
     * Получаем данные по всем курсам валют на вчерашний день
     * @param appID идентификатор для аутентификации на внешнем сервере курсов валют
     * @return данные по курсам валют сохраненные в объекте ExchangeRateBean
     */
    public ExchangeRateBean getYesterdayExchangeRate(String appID) {
        // Получаем вчерашнюю дату по UTC внешнего сервера
        String yesterday = LocalDate.now(ZoneId.of("UTC")).minusDays(1).toString();

        return client.getYesterdayExchangeRate(yesterday, appID);
    }

    /**
     * Получаем код и название всех курсов валют
     * @return Map, где key - код валюты, value - название валюты
     */
    public Map<String, String> getAllCodesExchangeRate() {
        return client.getAllCodesExchangeRate();
    }

    /**
     * Определяем стала ли валюта выше по отношению к базовой за последний день
     * @param currency код валюты у которой проверяется отношение к курсу base
     * @param appID идентификатор для аутентификации на внешнем сервере курсов валют
     * @return true если курс currency стал выше по отношению к курсу base, иначе false
     */
    public boolean isCurrencyRateHigherThanBaseRate(String currency, String appID) {
        // Проверяем наличие кода валюты на внешнем сервере
        Map<String, String> exchangeRatesMap = getAllCodesExchangeRate();
        if (!exchangeRatesMap.containsKey(currency))
            throw new NoSuchExchangeRatesException("Currency code is incorrect." +
                    " To view the current data, follow the link: http://localhost:8080/currencies");

        // Получаем данные по курсам за сегодня и вчера
        ExchangeRateBean exchangeRateToday = getExchangeRate(appID);
        ExchangeRateBean exchangeRateYesterday = getYesterdayExchangeRate(appID);

        RoundingMode halfUp = RoundingMode.HALF_UP;

        // Вычисляем сегодняшний курс currency по отношению к курсу base
        BigDecimal rateToday = BigDecimal.ONE
                .divide(BigDecimal.valueOf(exchangeRateToday.getRates().get(base)), 10, halfUp);
        Double currencyRateToday = exchangeRateToday.getRates().get(currency);
        currencyRateToday = rateToday.multiply(BigDecimal.valueOf(currencyRateToday)).doubleValue();

        // Вычисляем вчерашний курс currency по отношению к курсу base
        BigDecimal rateYesterday = BigDecimal.ONE
                .divide(BigDecimal.valueOf(exchangeRateYesterday.getRates().get(base)), 10, halfUp);
        Double currencyRateYesterday = exchangeRateYesterday.getRates().get(currency);
        currencyRateYesterday = rateYesterday.multiply(BigDecimal.valueOf(currencyRateYesterday)).doubleValue();

        return currencyRateToday < currencyRateYesterday;
    }
}
