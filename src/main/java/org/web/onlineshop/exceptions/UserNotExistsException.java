package org.web.onlineshop.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.web.onlineshop.util.UserRole;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserNotExistsException extends RuntimeException 
{
	private static final long serialVersionUID = 7255198334036850047L;
	private static Map<UserRole, String> userRoleMap = new HashMap<>();
	
	static 
	{
		userRoleMap.put(UserRole.ADMINISTRATOR, "Administrator");
		userRoleMap.put(UserRole.DELIVERER, "Deliverer");
		userRoleMap.put(UserRole.CUSTOMER, "Customer");
	}
	
	public UserNotExistsException(Long id, UserRole userRole) 
	{
		super(userRoleMap.get(userRole) + " with the id '" + id + "' does not exist!");
	}
}