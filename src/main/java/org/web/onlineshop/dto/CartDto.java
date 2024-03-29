package org.web.onlineshop.dto;

import java.time.LocalDateTime;

import org.web.onlineshop.util.OrderStatus;

import lombok.Data;

@Data
public class CartDto 
{
	private Long id;
    private LocalDateTime timestamp;
    private CustomerDto customer;
    private UserDto deliverer;
    private OrderStatus state;
    private Double totalPrice;
}