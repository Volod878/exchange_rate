package ru.volod878.exchange_rate.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Класс-клиент для обращения к внешнему серверу Giphy
 */
@FeignClient(name = "giphy", url = "${giphy.url}")
public interface GiphyClient {

    /**
     * Получаем с внешнего сервера данные с информацией об одном случайном gif который соответствует тегу
     * @param apiKey ключ для аутентификации на внешнем сервере
     * @param tag тег по которому будем искать gif
     * @return Map с информацией о gif
     */
    @GetMapping("${giphy.gifs.random}")
    Map<String, Map<String, ?>> getRandomGifByTag(@RequestParam("api_key") String apiKey,
                                             @RequestParam("tag") String tag);
}
