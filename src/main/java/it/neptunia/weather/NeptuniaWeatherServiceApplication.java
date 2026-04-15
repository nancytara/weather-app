package it.neptunia.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NeptuniaWeatherServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeptuniaWeatherServiceApplication.class, args);
	}

}
