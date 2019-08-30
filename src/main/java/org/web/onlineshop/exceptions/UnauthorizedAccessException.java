package org.web.onlineshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedAccessException extends RuntimeException 
{
	private static final long serialVersionUID = -8821570475968052918L;

	public UnauthorizedAccessException() {
        super("Unauthorized access to this resource!");
    }
}