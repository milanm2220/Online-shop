/**
 * Controller for retrieving articles
 */

webShopApp.controller('articleController', function ($scope, $timeout, $window, articlesFactory, customersFactory, articleCategoriesFactory) 
{  	
	$scope.role = $window.localStorage.getItem(ROLE_KEY);	
	
	$scope.$watch(function()
	{
	  return $window.localStorage.getItem(ROLE_KEY);
	},
	function(newValue, oldValue)
	{
		$scope.role = $window.localStorage.getItem(ROLE_KEY);
		getFavouriteArticles();
	});
	
	const getFavouriteArticles = function()
	{
		if ($scope.role == 'CUSTOMER')
		{
			customersFactory.getFavouriteArticles($window.localStorage.getItem(ID_KEY)).then(function(data)
			{
				$scope.favouriteArticles = data.data;
			})
			.catch(function(error) 
			{
				displayFailureMessage($scope, $timeout, error.data.message);;
			});
		}
	}
	
	getFavouriteArticles();
	
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
	
	articleCategoriesFactory.getArticleCategories().then(function(data)
	{
		$scope.categories = data.data;
	})
	.catch(function(error) 
	{
		displayFailureMessage($scope, $timeout, error.data.message);;
	});
	
	const getUnsortedArticles = function()
	{
		articlesFactory.getArticles().then(function(data)
		{
			$scope.articles = data.data;
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);;
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
				displayFailureMessage($scope, $timeout, error.data.message);;
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
				displayFailureMessage($scope, $timeout, error.data.message);;
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
			displayFailureMessage($scope, $timeout, error.data.message);;
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
				displayFailureMessage($scope, $timeout, error.data.message);;
			});
		}
		else
		{
			getUnsortedArticles();
		}
	}
	
	$scope.favouriteArticle = function(article)
	{
		let indexInFavourites = $scope.isInFavourites(article);
		
		if (indexInFavourites != -1)
		{
			customersFactory.removeArticleFromFavourites(article, $window.localStorage.getItem(ID_KEY)).then(function(data) 
			{
				$scope.favouriteArticles.splice(indexInFavourites, 1);
				displaySuccessMessage($scope, $timeout, 'Article \'' + article.name + '\' is removed from favourites.');
			})
			.catch(function(error) 
			{
				displayFailureMessage($scope, $timeout, error.data.message);;
			});
		}
		else
		{
			customersFactory.addArticleToFavourites(article, $window.localStorage.getItem(ID_KEY)).then(function(data) 
			{
				$scope.favouriteArticles.push(article);
				displaySuccessMessage($scope, $timeout, 'Article \'' + article.name + '\' is added in favourites.');
			})
			.catch(function(error) 
			{
				displayFailureMessage($scope, $timeout, error.data.message);;
			});
		}
	}
	
	$scope.isInFavourites = function(article)
	{
		for(x in $scope.favouriteArticles)
		{
			if ($scope.favouriteArticles[x].name == article.name)
			{
				return x;
			}
		}
		
		return -1;
	}
	
	$scope.onCartModalOpening = function(article)
	{
		$scope.itemToPutInCart = {"article" : article, "amount" : 1};
		$scope.article = article;
		$scope.itemAmount = 1;
	}
	
	$scope.newArticleCategoryChanged = function(category)
	{
		$scope.newArticle.category = category;
	}

	$scope.addArticleOpenModal = function()
	{
		$scope.title = 'New article';
		$scope.adding = true;
	}	
	
	$scope.addArticle = function()
	{
		$scope.submitted = true;
		
		if ($scope.articleForm.$invalid)
		{
			return;
		}
		
		for(category of $scope.categories)
		{
			if (category.id == $scope.newArticle.category)
			{
				$scope.newArticle.category = {id: category.id, name: category.name};
				break;
			}
		}
		
		articlesFactory.add($scope.newArticle).then(function(data)
		{
			$scope.articles.push(data.data);

			$('#articleModal').modal('hide');
			displaySuccessMessage($scope, $timeout, 'Article \'' + data.data.name + '\' is added successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);;
		});
	}
	
	$scope.editArticleOpenModal = function(index, article)
	{
		$scope.title = 'Update article';
		$scope.adding = false;
		$scope.articleEditIndex = index;
		
		$scope.newArticle = {};
		$scope.newArticle.id = article.id;
		$scope.newArticle.name = article.name;
		$scope.newArticle.price = article.price;
		$scope.newArticle.description = article.description;
		$scope.newArticle.quantity = article.quantity;
		$scope.newArticle.category = article.category;
		if (article.discount && article.discount > 0)
		{
			$scope.newArticle.discount = article.discount;		
		}
		$scope.newArticleCategory = article.category.id
	}
	
	$scope.editArticle = function()
	{
		$scope.submitted = true;
		
		if ($scope.articleForm.$invalid)
		{
			return;
		}
		
		for(category of $scope.categories)
		{
			if (category.id == $scope.newArticle.category)
			{
				$scope.newArticle.category = {id: category.id, name: category.name};
				break;
			}
		}
		
		articlesFactory.update($scope.newArticle).then(function(data)
		{
			$scope.articles.splice($scope.articleEditIndex, 1, data.data);

			$('#articleModal').modal('hide');
			displaySuccessMessage($scope, $timeout, 'Article \'' + data.data.name + '\' is updated successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);;
		});
	}
	
	$scope.deleteArticle = function(index, article)
	{	
		if (!confirm('Are you sure you want to remove article \'' + article.name + '\'?')) 
		{
			return;
		} 
		
		articlesFactory.delete(article.id).then(function(data)
		{
			$scope.articles.splice(index, 1);
			$scope.articleEditIndex
			displaySuccessMessage($scope, $timeout, 'Article \'' + article.name + '\' is removed successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);;
		});
	}
});