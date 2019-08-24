package org.web.onlineshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidCredentialsException extends RuntimeException 
{
	private static final long serialVersionUID = 5777581860012109626L;

	public InvalidCredentialsException() {
        super("Invalid username and/or password! Please try again.");
    }
}