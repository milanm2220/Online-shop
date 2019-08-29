package org.web.onlineshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ArticleAlreadyExistsException extends RuntimeException 
{
	private static final long serialVersionUID = -7547311675820116323L;

	public ArticleAlreadyExistsException(String name) {
        super("Article with name '" + name + "' already exists!");
    }
}