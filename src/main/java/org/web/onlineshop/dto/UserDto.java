package org.web.onlineshop.dto;

import org.web.onlineshop.model.UserRole;

import lombok.Data;

@Data
public class UserDto 
{
	private Long id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private UserRole role;
    private String phoneNumber;
    private String email;
    private String address;
}