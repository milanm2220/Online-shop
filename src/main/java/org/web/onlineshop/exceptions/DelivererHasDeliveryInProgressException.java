package org.web.onlineshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class DelivererHasDeliveryInProgressException extends RuntimeException 
{
	private static final long serialVersionUID = -7321418597079097837L;

	public DelivererHasDeliveryInProgressException(String username) {
        super("Deliverer '" + username + "' cannot be deleted because of delivery in progress!");
    }
}