/**
 * Instance of an AngularJS application.
 */

var webShopApp = angular.module('webShopApp', ['ngRoute']);

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