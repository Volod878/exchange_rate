package ru.volod878.exchange_rate.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.volod878.exchange_rate.model.ExchangeRateBean;

@FeignClient(name = "oxr", url = "${open_exchange_rate.url}")
public interface OpenExchangeRatesClient {

    @GetMapping("${open_exchange_rate.latest}")
    ExchangeRateBean getExchangeRate();

    @GetMapping("${open_exchange_rate.historical}")
    ExchangeRateBean getYesterdayExchangeRate();
}
