package org.web.onlineshop.controller;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web.onlineshop.dto.CredentialsDto;
import org.web.onlineshop.dto.UserDto;
import org.web.onlineshop.model.User;
import org.web.onlineshop.service.UserService;
import org.web.onlineshop.util.Constants;

@RestController
@RequestMapping(value = Constants.REST_API_PREFIX + "/users")
public class UserController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> getLoggedInUser()
	{
		User user = this.userService.getLoggedInUser();
		if (user == null)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		UserDto userDto = modelMapper.map(user, UserDto.class);
		return new ResponseEntity<>(userDto, HttpStatus.OK);	
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto)
	{
		try 
		{
			User user = this.userService.login(credentialsDto);
			UserDto userDto = modelMapper.map(user, UserDto.class);
			return new ResponseEntity<>(userDto, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> logout()
	{
		try 
		{
			this.userService.logout();
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
}