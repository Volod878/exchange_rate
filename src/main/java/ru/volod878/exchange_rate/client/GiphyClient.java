package ru.volod878.exchange_rate.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.volod878.exchange_rate.model.GiphyBean;

@FeignClient(name = "giphy", url = "${giphy.url}")
public interface GiphyClient {

    @GetMapping("${giphy.gifs.random}")
    GiphyBean getRandomGif(@RequestParam("api_key") String apiKey, @RequestParam("tag") String tag);
}
