package ru.volod878.exchange_rate.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.volod878.exchange_rate.exception_handling.exception.NoSuchExchangeRatesException;

/**
 * Класс-advice для управления исключениями
 */
@ControllerAdvice
public class ExchangeRateGlobalExceptionHandler {

    /**
     * Если код валюты не корректный. Выбрасывается исключение NoSuchExchangeRatesException
     * и срабатывает этот метод.
     * @param exception который был выброшен
     * @return информационное сообщение об исключении
     */
    @ExceptionHandler
    public ResponseEntity<ExchangeRatesIncorrectData> handleException(NoSuchExchangeRatesException exception) {
        ExchangeRatesIncorrectData data = new ExchangeRatesIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    /**
     * Метод выполняется когда было вызвано исключение Exception.
     * @param exception который был выброшен
     * @return информационное сообщение об исключении
     */
    @ExceptionHandler
    public ResponseEntity<ExchangeRatesIncorrectData> handleException(Exception exception) {

        ResponseEntity<ExchangeRatesIncorrectData> response;
        ExchangeRatesIncorrectData data = new ExchangeRatesIncorrectData();
        String message;

        // Проверяем причину исключения и http статус
        if (exception.getMessage().contains("Unauthorized")) {
            String exceptionMessage = exception.getMessage();
            message = exceptionMessage
                    .substring(exceptionMessage.indexOf("\"message\":") + 12)
                    .split("\"")[0];
            data.setInfo(message);
            response = new ResponseEntity<>(data, HttpStatus.UNAUTHORIZED);

        } else if (exception.getMessage().contains("Forbidden")) {
            String exceptionMessage = exception.getMessage();
            message = exceptionMessage
                    .substring(exceptionMessage.indexOf("\"message\":") + 11)
                    .split("\"")[0];
            data.setInfo(message);
            response = new ResponseEntity<>(data, HttpStatus.FORBIDDEN);

        } else {
            message = exception.getMessage();
            data.setInfo(message);
            response = new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        }

        return response;
    }
}
