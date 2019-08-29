package org.web.onlineshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class CantChangeDelivererRoleException extends RuntimeException 
{
	private static final long serialVersionUID = -5334340014075544459L;

	public CantChangeDelivererRoleException(String username) {
        super("Role of the deliverer '" + username + "' cannot be changed because of delivery in progress!");
    }
}