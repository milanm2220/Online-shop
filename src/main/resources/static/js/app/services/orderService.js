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
	
	factory.getDeliveredOrdersInPeriod = function(startDate, endDate) 
	{
		return $http.get('/api/carts/delivered?startDate=' + startDate + "&endDate=" + endDate);
	};	
	
	factory.getNumberOfCanceledOrdersInPeriod = function(startDate, endDate) 
	{
		return $http.get('/api/carts/canceled?startDate=' + startDate + "&endDate=" + endDate);
	};
	
	factory.getIncomeInPeriod = function(startDate, endDate) 
	{
		return $http.get('/api/carts/income?startDate=' + startDate + "&endDate=" + endDate);
	};
	
	return factory;
});