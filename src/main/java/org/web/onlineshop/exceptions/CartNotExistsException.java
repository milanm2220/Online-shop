package org.web.onlineshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CartNotExistsException extends RuntimeException 
{
	private static final long serialVersionUID = 1100326554719567380L;

	public CartNotExistsException(Long id) 
	{
		super("Cart with the id '" + id + "' does not exist!");
	}
}