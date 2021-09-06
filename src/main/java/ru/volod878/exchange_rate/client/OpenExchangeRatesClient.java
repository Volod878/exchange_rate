package ru.volod878.exchange_rate.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.volod878.exchange_rate.model.ExchangeRateBean;

import java.util.Map;

/**
 * Класс-клиент для обращения к внешнему серверу OpenExchangeRates
 */
@FeignClient(name = "oxr", url = "${open_exchange_rate.url}")
public interface OpenExchangeRatesClient {

    /**
     * Получаем данные с внешнего сервера по всем курсам валют на сегодняшний день
     * @param appID идентификатор для аутентификации на внешнем сервере курсов валют
     * @return данные по курсам валют сохраненные в объекте ExchangeRateBean
     */
    @GetMapping("${open_exchange_rate.latest}")
    ExchangeRateBean getExchangeRate(@RequestParam("app_id") String appID);

    /**
     * Получаем данные с внешнего сервера по всем курсам валют на указанную дату
     * @param date получим курсы валют на эту дату
     * @param appID идентификатор для аутентификации на внешнем сервере курсов валют
     * @return данные по курсам валют сохраненные в объекте ExchangeRateBean
     */
    @GetMapping("${open_exchange_rate.historical}/{date}.json")
    ExchangeRateBean getYesterdayExchangeRate(@PathVariable(name = "date") String date,
                                              @RequestParam("app_id") String appID);

    /**
     * Получаем с внешнего сервера код и название всех курсов валют
     * @return Map, где key - код валюты, value - название валюты
     */
    @GetMapping("${open_exchange_rate.currencies}")
    Map<String, String> getAllCodesExchangeRate();
}
