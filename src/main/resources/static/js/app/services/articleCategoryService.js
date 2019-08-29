/**
 * Service for article categories functions
 */

webShopApp.factory('articleCategoriesFactory', function($http)
{	
	let factory = {};
	
	factory.add = function(articleCategory) 
	{
		return $http.post('/api/article_categories', articleCategory);
	};
	
	factory.update = function(articleCategory) 
	{
		return $http.put('/api/article_categories', articleCategory);
	};
	
	factory.delete = function(id) 
	{
		return $http.delete('/api/article_categories/' + id);
	};
	
	factory.getArticleCategories = function() 
	{
		return $http.get('/api/article_categories');
	};
	
	return factory;
});