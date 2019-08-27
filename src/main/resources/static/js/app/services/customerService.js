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
	
	return factory;
});