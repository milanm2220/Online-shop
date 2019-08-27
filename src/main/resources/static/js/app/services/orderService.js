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
	
	return factory;
});