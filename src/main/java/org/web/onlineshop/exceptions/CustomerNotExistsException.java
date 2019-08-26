package org.web.onlineshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CustomerNotExistsException extends RuntimeException 
{
	private static final long serialVersionUID = 7255198334036850047L;
	
	public CustomerNotExistsException(Long id) 
	{
		super("Customer with the id '" + id + "' does not exist!");
	}
}