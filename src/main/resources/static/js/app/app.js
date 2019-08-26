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
	.otherwise(
	{
		redirectTo: '/'
	});	
});

const USERNAME_KEY = 'logged_in_user_username';
const ROLE_KEY = 'logged_in_user_role';