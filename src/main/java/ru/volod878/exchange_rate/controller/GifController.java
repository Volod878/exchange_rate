package ru.volod878.exchange_rate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.volod878.exchange_rate.client.GiphyClient;

import java.util.Map;

/**
 * Класс-контроллер для работы с данными gif
 */
@RestController
@RequestMapping("/gif")
public class GifController {

    private final GiphyClient client;

    @Autowired
    public GifController(GiphyClient client) {
        this.client = client;
    }

    /**
     * Получаем данные с информацией об одном случайном gif который соответствует тегу
     * @param apiKey ключ для аутентификации на внешнем сервере
     * @param tag тег по которому будем искать gif
     * @return представление gif
     */
    @GetMapping(value = "/{tag}")
    public RedirectView getRandomGifByTag(@RequestParam("api_key") String apiKey, @PathVariable("tag") String tag) {
        Map<String, ?> data = client.getRandomGifByTag(apiKey, tag).get("data");
        return new RedirectView(data.get("image_url").toString());
    }
}
