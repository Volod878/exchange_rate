package ru.volod878.exchange_rate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.volod878.exchange_rate.client.GiphyClient;
import ru.volod878.exchange_rate.model.GiphyBean;

@RestController
@RequestMapping("/gif")
public class GifController {

    private final GiphyClient client;

    @Autowired
    public GifController(GiphyClient client) {
        this.client = client;
    }

    @GetMapping(value = "/{tag}")
    public RedirectView getRichGif(@RequestParam("api_key") String apiKey, @PathVariable("tag") String tag) {
        GiphyBean giphyBean = client.getRandomGif(apiKey, tag);
        return new RedirectView(giphyBean.getData().get("image_url").toString());
    }
}
