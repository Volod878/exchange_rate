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
import java.util.TreeMap;

/**
 * Класс-сервис.
 * Обрабатывает данные полученные с клиента и передает в контроллер
 */
@Service
public class ExchangeRateService {

    @Value("${exchange_rate.base}")
    private String base;

    @Value("${server.port}")
    private String port;

    private final OpenExchangeRatesClient client;

    @Autowired
    public ExchangeRateService(OpenExchangeRatesClient client) {
        this.client = client;
    }

    /**
     * Получаем данные по всем курсам валют на сегодняшний день.
     * Устанавливаем переменную base в качестве базовой валюты.
     * @param appID идентификатор для аутентификации на внешнем сервере курсов валют
     * @return данные по курсам валют сохраненные в объекте ExchangeRateBean
     */
    public ExchangeRateBean getExchangeRate(String appID) {
        ExchangeRateBean tempRateBean = client.getExchangeRate(appID);

        return setBaseCurrency(tempRateBean);
    }

    /**
     * Получаем данные по всем курсам валют на вчерашний день.
     * Устанавливаем переменную base в качестве базовой валюты.
     * @param appID идентификатор для аутентификации на внешнем сервере курсов валют
     * @return данные по курсам валют сохраненные в объекте ExchangeRateBean
     */
    public ExchangeRateBean getYesterdayExchangeRate(String appID) {
        // Получаем вчерашнюю дату по UTC внешнего сервера
        String yesterday = LocalDate.now(ZoneId.of("UTC")).minusDays(1).toString();

        ExchangeRateBean tempRateBean = client.getYesterdayExchangeRate(yesterday, appID);

        return setBaseCurrency(tempRateBean);
    }

    /**
     * Устанавливаем переменную base в качестве базовой валюты.
     * @param exchangeRateBean модель кусов валют с внешнего сервера
     * @return новую модель ExchangeRateBean с базовой валютой base
     */
    private ExchangeRateBean setBaseCurrency(ExchangeRateBean exchangeRateBean) {
        String disclaimer = exchangeRateBean.getDisclaimer();
        String license = exchangeRateBean.getLicense();
        long timestamp = exchangeRateBean.getTimestamp();
        base = base.toUpperCase();
        Map<String, Double> rates = new TreeMap<>();

        RoundingMode halfUp = RoundingMode.HALF_UP;
        BigDecimal rate = BigDecimal.ONE
                .divide(BigDecimal.valueOf(exchangeRateBean.getRates().get(base)), 20, halfUp);

        exchangeRateBean.getRates().forEach((k, v) ->
                rates.put(k, rate.multiply(
                        BigDecimal.valueOf(v)).doubleValue())
        );

        return new ExchangeRateBean(disclaimer, license, timestamp, base, rates);
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
                    " To view the current data, follow the link: http://localhost:" + port + "/currencies");

        // Получаем данные по курсам за последние два дня
        ExchangeRateBean exchangeRateToday = getExchangeRate(appID);
        ExchangeRateBean exchangeRateYesterday = getYesterdayExchangeRate(appID);

        return exchangeRateToday.getRates().get(currency) < exchangeRateYesterday.getRates().get(currency);
    }
}
