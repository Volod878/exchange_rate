package ru.volod878.exchange_rate.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.volod878.exchange_rate.model.ExchangeRateBean;

@FeignClient(name = "oxr", url = "${open_exchange_rate.url}")
public interface OpenExchangeRatesClient {

    @GetMapping("${open_exchange_rate.latest}")
    ExchangeRateBean getExchangeRate(@RequestParam("app_id") String appID);

    @GetMapping("${open_exchange_rate.historical}/{yesterday}.json")
    ExchangeRateBean getYesterdayExchangeRate(@PathVariable(name = "yesterday") String yesterday,
                                              @RequestParam("app_id") String appID);
}
