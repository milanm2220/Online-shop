package org.web.onlineshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException 
{
	private static final long serialVersionUID = -3670901425139332417L;

    public UserAlreadyExistsException(String username, String email) {
        super("Username '" + username + "' or email '" + email + "' is already in use!");
    }
}