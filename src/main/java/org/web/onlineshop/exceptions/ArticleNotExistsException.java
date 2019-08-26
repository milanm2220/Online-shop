package org.web.onlineshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ArticleNotExistsException extends RuntimeException 
{
	private static final long serialVersionUID = -5116614990678676153L;

	public ArticleNotExistsException(Long id) 
	{
		super("Article with the id '" + id + "' does not exist!");
	}
}