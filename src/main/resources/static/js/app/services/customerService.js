/**
 * Service for customers functions
 */

webShopApp.factory('customersFactory', function($http)
{	
	let factory = {};
	
	factory.getFavouriteArticles = function(id) 
	{
		return $http.get('/api/customers/favourite_articles/' + id);
	};
	
	factory.addArticleToFavourites = function(article, id)
	{
		return $http.put('/api/customers/favourite_articles/add/' + id, article);
	}
	
	factory.removeArticleFromFavourites = function(article, id)
	{
		return $http.put('/api/customers/favourite_articles/remove/' + id, article);
	}
	
	factory.getCart = function(id) 
	{
		return $http.get('/api/customers/cart/' + id);
	};
	
	factory.addItemToCart = function(item, id)
	{
		return $http.put('/api/customers/cart/add/' + id, item);
	}
	
	factory.dropItemFromCart = function(item, id)
	{
		return $http.put('/api/customers/cart/drop/' + id, item);
	}
	
	factory.order = function(id, bonusPoints) 
	{
		let path = '/api/customers/cart/' + id;
		
		if (bonusPoints && bonusPoints > 0)
		{
			path += '?bonusPoints=' + bonusPoints;
		}
		
		return $http.put(path);
	};
	
	factory.getOrders = function(id) 
	{
		return $http.get('/api/customers/cart/all/' + id);
	};
	
	factory.getBonusPoints = function(id) 
	{
		return $http.get('/api/customers/bonusPoints/' + id);
	};
	
	return factory;
});