package ru.volod878.exchange_rate.model;

import java.util.Map;
import java.util.Objects;

/**
 * Класс-модель курсов валют
 */
public class ExchangeRateBean {
    private String disclaimer;
    private String license;
    private long timestamp;
    private String base;
    private Map<String, Double> rates;

    public ExchangeRateBean() {
    }

    public ExchangeRateBean(String disclaimer, String license, long timestamp, String base, Map<String, Double> rates) {
        this.disclaimer = disclaimer;
        this.license = license;
        this.timestamp = timestamp;
        this.base = base;
        this.rates = rates;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeRateBean)) return false;
        ExchangeRateBean that = (ExchangeRateBean) o;
        return getTimestamp() == that.getTimestamp()
                && Objects.equals(getDisclaimer(), that.getDisclaimer())
                && Objects.equals(getLicense(), that.getLicense())
                && Objects.equals(getBase(), that.getBase())
                && Objects.equals(getRates(), that.getRates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDisclaimer(), getLicense(), getTimestamp(), getBase(), getRates());
    }
}
