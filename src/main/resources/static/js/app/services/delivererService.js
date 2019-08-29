/**
 * Service for deliverers functions
 */

webShopApp.factory('deliverersFactory', function($http)
{	
	let factory = {};

	factory.getDeliverers = function() 
	{
		return $http.get('/api/deliverers');
	};
	
	factory.add = function(deliverer) 
	{
		return $http.post('/api/deliverers', deliverer);
	};
	
	factory.update = function(deliverer) 
	{
		return $http.put('/api/deliverers', deliverer);
	};
	
	factory.delete = function(id) 
	{
		return $http.delete('/api/deliverers/' + id);
	};
	
	factory.takeOverOrder = function(id, cart) 
	{
		return $http.put('/api/deliverers/cart/take_over/' + id, cart);
	};
	
	factory.deliverOrder = function(id, cart) 
	{
		return $http.put('/api/deliverers/cart/deliver/' + id, cart);
	};
	
	factory.cancelOrder = function(id, cart) 
	{
		return $http.put('/api/deliverers/cart/cancel/' + id, cart);
	};
	
	factory.getOrders = function(id) 
	{
		return $http.get('/api/deliverers/cart/all/' + id);
	};	
	
	factory.getOrderInProgress = function(id) 
	{
		return $http.get('/api/deliverers/cart/in_progress/' + id);
	};	

	return factory;
});