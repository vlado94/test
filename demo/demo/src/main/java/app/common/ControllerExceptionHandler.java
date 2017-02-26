package app.common;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "resourceNotFound")
	@ExceptionHandler(value = { NoSuchElementException.class, NullPointerException.class })
	public void notFound() {
		// For exceptions
	}

}