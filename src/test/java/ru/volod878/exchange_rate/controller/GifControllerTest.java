package ru.volod878.exchange_rate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.volod878.exchange_rate.client.GiphyClient;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

@DisplayName("Unit-level testing for GifControllerTest")
public class GifControllerTest {

    private GifController controller;

    @Mock
    private GiphyClient client;

    private final Map<String, Map<String, ?>> gifData = new HashMap<>();
    private final Map<String,String> data = new HashMap<>();

    @BeforeEach
    public void init() {
        openMocks(this);

        controller = new GifController(client);

        data.put("image_url", "https://media4.giphy.com/media/N6Z3k42z8ln3XFlRDd/giphy.gif");
        gifData.put("data", data);
    }

    @Test
    public void shouldReturnCorrectURL() {
        String apiKey = "correct-api_key";
        String tag = "any tag";

        given(client.getRandomGifByTag(apiKey, tag)).willReturn(gifData);

        assertEquals(controller.getRandomGifByTag(apiKey, tag).getUrl(),
                "https://media4.giphy.com/media/N6Z3k42z8ln3XFlRDd/giphy.gif");

    }
}
