package ru.volod878.exchange_rate.model;

import java.util.Map;

public class GiphyBean {

    private Map<String, ?> data;

    public GiphyBean() {
    }

    public GiphyBean(Map<String, ?> data) {
        this.data = data;
    }

    public Map<String, ?> getData() {
        return data;
    }

    public void setData(Map<String, ?> data) {
        this.data = data;
    }
}
