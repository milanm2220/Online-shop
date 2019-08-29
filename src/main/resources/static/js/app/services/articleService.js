/**
 * Service for articles functions
 */

webShopApp.factory('articlesFactory', function($http)
{	
	let factory = {};
	
	factory.add = function(article) 
	{
		return $http.post('/api/articles', article);
	};
	
	factory.update = function(article) 
	{
		return $http.put('/api/articles', article);
	};
	
	factory.delete = function(id) 
	{
		return $http.delete('/api/articles/' + id);
	};
	
	factory.getArticles = function() 
	{
		return $http.get('/api/articles');
	};
	
	factory.getArticlesOnDiscount = function() 
	{
		return $http.get('/api/articles/discount');
	};
	
	factory.getArticlesByCategory = function(categoryId) 
	{
		return $http.get('/api/articles/category/' + categoryId);
	};
	
	factory.getSortedArticles = function(sortType) 
	{
		return $http.get('/api/articles/sort/' + sortType);
	};
	
	factory.searchArticles = function(name, description, priceFrom, priceTo) 
	{
		let path = '/api/articles/search';
		let firstParameterPassed = false;
		
		if (name && name.length > 0)
		{
			path = path.concat('?name=' + name);
			firstParameterPassed = true;
		}
		
		if (description && description.length > 0)
		{
			if (!firstParameterPassed)
			{
				path = path.concat('?description=' + description);
				firstParameterPassed = true;
			}
			else
			{
				path = path.concat('&description=' + description);
			}
		}
		
		if (priceFrom && !isNaN(priceFrom))
		{
			if (!firstParameterPassed)
			{
				path = path.concat('?priceFrom=' + priceFrom);
				firstParameterPassed = true;
			}
			else
			{
				path = path.concat('&priceFrom=' + priceFrom);
			}
		}
		
		if (priceTo && !isNaN(priceTo))
		{
			if (!firstParameterPassed)
			{
				path = path.concat('?priceTo=' + priceTo);			
			}
			else
			{
				path = path.concat('&priceTo=' + priceTo);
			}
		}
		
		return $http.get(path);
	};
	
	return factory;
});