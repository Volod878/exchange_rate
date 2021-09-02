package ru.volod878.exchange_rate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.volod878.exchange_rate.client.OpenExchangeRatesClient;
import ru.volod878.exchange_rate.model.ExchangeRateBean;


@RestController
public class ExchangeRateController {

    private final OpenExchangeRatesClient client;

    @Autowired
    public ExchangeRateController(OpenExchangeRatesClient client) {
        this.client = client;
    }

    @GetMapping("/")
    public ExchangeRateBean getExchangeRate() {
        return client.getExchangeRate();
    }
}
