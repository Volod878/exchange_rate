package ru.volod878.exchange_rate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.volod878.exchange_rate.exception_handling.exception.NoSuchExchangeRatesException;
import ru.volod878.exchange_rate.model.ExchangeRateBean;
import ru.volod878.exchange_rate.service.ExchangeRateService;

import java.util.Map;

/**
 * Класс-контроллер для работы с данными курсов валют
 */
@RestController
public class ExchangeRateController {

    private final ExchangeRateService service;

    @Autowired
    public ExchangeRateController(ExchangeRateService service) {
        this.service = service;
    }

    /**
     * Получаем данные по всем курсам валют на сегодняшний день
     * @param appID идентификатор для аутентификации на внешнем сервере курсов валют
     * @return JSON по курсам валют сохраненные в объекте ExchangeRateBean
     */
    @GetMapping("/")
    public ExchangeRateBean getExchangeRate(@RequestParam("app_id") String appID) {
        return service.getExchangeRate(appID);
    }

    /**
     * Получаем данные по всем курсам валют на вчерашний день
     * @param appID идентификатор для аутентификации на внешнем сервере курсов валют
     * @return JSON по курсам валют сохраненные в объекте ExchangeRateBean
     */
    @GetMapping("/yesterday")
    public ExchangeRateBean getYesterdayExchangeRate(@RequestParam("app_id") String appID) {
        return service.getYesterdayExchangeRate(appID);
    }

    /**
     * Получаем код и название всех курсов валют
     * @return JSON с кодами курсов валют и их названием
     */
    @GetMapping("/currencies")
    public Map<String, String> getAllCodesExchangeRate() {
        return service.getAllCodesExchangeRate();
    }

    /**
     * Получаем статус в формате boolean отношения курса currency к базовому курсу
     * @param currency код валюты у которой проверяется отношение к базовому курсу
     * @param appID идентификатор для аутентификации на внешнем сервере курсов валют
     * @param apiKey ключ для аутентификации на внешнем сервере
     * @return если статус true возвращаем представление gif по тегу rich,
     * иначе - представление gif по тегу broke
     */
    @GetMapping("/currencies/{currency}")
    public RedirectView getGifByStatus(@PathVariable(name = "currency") String currency,
                                       @RequestParam("app_id") String appID,
                                       @RequestParam("api_key") String apiKey) {

        if (currency.length() != 3)
            throw new NoSuchExchangeRatesException("Currency code must be three characters");

        return service.isCurrencyRateHigherThanBaseRate(currency.toUpperCase(), appID) ?
                new RedirectView("/gif/rich?api_key=" + apiKey) :
                new RedirectView("/gif/broke?api_key=" + apiKey);
    }
}
