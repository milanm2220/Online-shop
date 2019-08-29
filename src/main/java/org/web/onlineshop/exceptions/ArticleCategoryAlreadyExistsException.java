package org.web.onlineshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ArticleCategoryAlreadyExistsException extends RuntimeException 
{
	private static final long serialVersionUID = 2835183567625049920L;

	public ArticleCategoryAlreadyExistsException(String name) {
        super("Article category with name '" + name + "' already exists!");
    }
}