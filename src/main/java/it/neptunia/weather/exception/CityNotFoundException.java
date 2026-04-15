package it.neptunia.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6774224641774523330L;

	public CityNotFoundException(String city) {
		super("Città non trovata: " + city);
	}
}