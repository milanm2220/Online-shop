package org.web.onlineshop.dto;

import org.web.onlineshop.util.UserRole;

import lombok.Data;

@Data
public class RoleChangeDto 
{
	private Long id;
	private UserRole oldRole;
	private UserRole newRole;
}