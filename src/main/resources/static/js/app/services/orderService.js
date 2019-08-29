/**
 * Service for articles ordering functions
 */

webShopApp.factory('ordersFactory', function($http)
{	
	let factory = {};
	
	factory.getCartItems = function(id) 
	{
		return $http.get('/api/carts/items/' + id);
	};
	
	factory.getOrdersInOrderedState = function() 
	{
		return $http.get('/api/carts/ordered');
	};
	
	factory.getOrdersInProgress = function() 
	{
		return $http.get('/api/carts/in_progress');
	};
	
	return factory;
});