package ru.volod878.exchange_rate.exception_handling.exception;

public class NoSuchExchangeRatesException extends RuntimeException {

    public NoSuchExchangeRatesException(String message) {
        super(message);
    }
}
