/**
 * Service for users functions
 */

webShopApp.factory('usersFactory', function($http)
{	
	let factory = {};
	
	factory.registerUser = function(user) 
	{
		return $http.post('/api/customers', user);
	};
	
	factory.getLoggedInUser = function() 
	{
		return $http.get('/api/users');
	};
	
	factory.login = function(credentials) 
	{
		return $http.post('/api/users', credentials);
	};
	
	factory.logout = function() 
	{
		return $http.post('/api/users/logout');
	};
	
	factory.showPassword = function myFunction(input) 
	{
	    if (input.type === "password") 
	    {
	    	input.type = "text";
	    }
	    else
	    {
	    	input.type = "password";
	    }
	};
	
	return factory;
});