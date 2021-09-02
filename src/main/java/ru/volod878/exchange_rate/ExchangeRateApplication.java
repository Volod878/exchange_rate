package ru.volod878.exchange_rate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("ru.volod878.exchange_rate.client")
public class ExchangeRateApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeRateApplication.class, args);
    }

}
