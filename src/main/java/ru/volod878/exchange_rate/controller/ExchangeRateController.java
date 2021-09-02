package ru.volod878.exchange_rate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.volod878.exchange_rate.model.ExchangeRateBean;
import ru.volod878.exchange_rate.service.ExchangeRateService;

@RestController
public class ExchangeRateController {

    private final ExchangeRateService service;

    @Autowired
    public ExchangeRateController(ExchangeRateService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ExchangeRateBean getExchangeRate(@RequestParam("app_id") String appID) {
        return service.getExchangeRate(appID);
    }

    @GetMapping("/yesterday")
    public ExchangeRateBean getYesterdayExchangeRate(@RequestParam("app_id") String appID) {
        return service.getYesterdayExchangeRate(appID);
    }

    @GetMapping("/{currency}")
    public RedirectView getGifByStatus(@PathVariable(name = "currency") String currency,
                                       @RequestParam("app_id") String appID,
                                       @RequestParam("api_key") String apiKey) {

        return service.isCurrencyRateHigherThanBaseRate(currency, appID) ?
                new RedirectView("/gif/rich?api_key=" + apiKey) :
                new RedirectView("/gif/broke?api_key=" + apiKey);
    }
}
