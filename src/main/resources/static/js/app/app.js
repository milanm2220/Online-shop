/**
 * Instance of an AngularJS application.
 */

let webShopApp = angular.module('webShopApp', ['ngRoute']);

webShopApp.config(function($routeProvider, $locationProvider) 
{	
	$locationProvider.hashPrefix('');
	$routeProvider.when('/',
	{
		templateUrl: 'pages/home.html'
	})
	$routeProvider.when('/profile',
	{
		templateUrl: 'pages/profile.html'
	})
	.otherwise(
	{
		redirectTo: '/'
	});	
});

const ID_KEY = 'logged_in_user_id';
const USERNAME_KEY = 'logged_in_user_username';
const ROLE_KEY = 'logged_in_user_role';

webShopApp.filter('range', function() 
{
    return function(input, start, end) 
    {    
  	    start = parseInt(start);
	    end = parseInt(end);
	    while (start != end) 
	    {
		    input.push(start);
	        start += 1;
	    }
	    return input;
    };
});

webShopApp.filter('enumFilter', function() 
{
	return function(x) 
	{
		if (!x)
		{
			return "";
		}
	    return (x.charAt(0).toUpperCase() + x.slice(1).toLowerCase()).replace(/_/g, ' ');
	};
});