/**
 * Controller for retrieving articles
 */

webShopApp.controller('articleController', function ($scope, $timeout, articlesFactory) 
{  
	$scope.sortTypes = [
		{
			"value" : "name_asc",
		    "name" : "Name (asc)"
		},
		{
			"value" : "name_desc",
			"name" : "Name (desc)"
		},
		{
			"value" : "price_asc",
			"name" : "Price (asc)"
		},
		{
			"value" : "price_desc",
			"name" : "Price (desc)"
		}
	];
	
	articlesFactory.getArticleCategories().then(function(data)
	{
		$scope.categories = data.data;
	})
	.catch(function(error) 
	{
		displayFailureMessage();
	});
	
	const displayFailureMessage = function()
	{
		$scope.failAlertMessage = error.data.message;
		$scope.failAlertVisibility = true;
		
		$timeout(function ()
		{
			$scope.failAlertVisibility = false;
		}, 3000);
	}
	
	const getUnsortedArticles = function()
	{
		articlesFactory.getArticles().then(function(data)
		{
			$scope.articles = data.data;
		})
		.catch(function(error) 
		{
			displayFailureMessage();
		});
	}
	
	getUnsortedArticles();
	
	$scope.sortTypeChanged = function(sortType)
	{
		if (sortType)
		{
			articlesFactory.getSortedArticles(sortType).then(function(data)
			{
				$scope.articles = data.data;
			})
			.catch(function(error) 
			{
				displayFailureMessage();
			});
		}
		else
		{
			getUnsortedArticles();
		}
	}
	
	$scope.displayOnSale = function(onSale)
	{
		if (!onSale)
		{
			articlesFactory.getArticlesOnDiscount().then(function(data)
			{
				$scope.articles = data.data;
			})
			.catch(function(error) 
			{
				displayFailureMessage();
			});
		}
		else
		{
			getUnsortedArticles();
		}
	}
	
	$scope.search = function(name, description, priceFrom, priceTo)
	{
		articlesFactory.searchArticles(name, description, priceFrom, priceTo).then(function(data)
		{
			$scope.articles = data.data;
		})
		.catch(function(error) 
		{
			displayFailureMessage();
		});
	}
	
	$scope.categoryChanged = function(categoryId)
	{
		if (categoryId)
		{
			articlesFactory.getArticlesByCategory(categoryId).then(function(data)
			{
				$scope.articles = data.data;
			})
			.catch(function(error) 
			{
				displayFailureMessage();
			});
		}
		else
		{
			getUnsortedArticles();
		}
	}
});