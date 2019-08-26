package org.web.onlineshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ArticleCategoryNotExistsException extends RuntimeException 
{
	private static final long serialVersionUID = -1354371606618394458L;

	public ArticleCategoryNotExistsException(Long id) 
	{
		super("Article category with the id '" + id + "' does not exist!");
	}
}