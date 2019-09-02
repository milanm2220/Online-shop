/**
 * Controller for displaying user profile info
 */

webShopApp.controller('profileController', function($scope, $window, $timeout, usersFactory, customersFactory, deliverersFactory, ordersFactory, articlesFactory, articleCategoriesFactory) 
{	
	const getAdministratorData = function(id)
	{
		articleCategoriesFactory.getArticleCategories().then(function(data)
		{
			$scope.categories = data.data;
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
		
		deliverersFactory.getDeliverers().then(function(data)
		{
			$scope.deliverers = data.data;
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
		
		usersFactory.getNonAdminUsers().then(function(data)
		{
			$scope.users = data.data;
			for (let index in $scope.users)
			{
				$scope.users[index].originalRole = $scope.users[index].role;
			}
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
		
		$scope.userRoles = [
			{
				"value" : "ADMINISTRATOR",
				"name" : "Administrator"
			},
			{
				"value" : "CUSTOMER",
			    "name" : "Customer"
			},
			{
				"value" : "DELIVERER",
				"name" : "Deliverer"
			}
		];
	}
	
	const getCustomerData = function(id)
	{
		customersFactory.getOrders($scope.user.id).then(function(data)
		{
			$scope.orders = data.data;
			$scope.orders.sort((a,b) => b.timestamp.localeCompare(a.timestamp));
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
		
		customersFactory.getFavouriteArticles($scope.user.id).then(function(data)
		{
			$scope.articles = data.data;
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	const getDelivererData = function(id)
	{
		ordersFactory.getOrdersInOrderedState().then(function(data)
		{
			$scope.orderedOrders = data.data;
			$scope.orderedOrders.sort((a,b) => b.timestamp.localeCompare(a.timestamp));
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
		
		deliverersFactory.getOrders(id).then(function(data)
		{
			$scope.orders = data.data;
			$scope.orders.sort((a,b) => b.timestamp.localeCompare(a.timestamp));
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});

		deliverersFactory.getOrderInProgress(id).then(function(data)
		{
			$scope.deliveryInProgress = data.data;
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	usersFactory.getLoggedInUser().then(function(data) 
	{
		$scope.user = data.data;
		
		if ($scope.user.role == 'ADMINISTRATOR')
		{
			getAdministratorData($scope.user.id);
		}
		else if ($scope.user.role == 'CUSTOMER')
		{
			getCustomerData($scope.user.id);
		}
		else if ($scope.user.role == 'DELIVERER')
		{
			getDelivererData($scope.user.id);
		}
	})
	.catch(function(error) 
	{
		displayFailureMessage($scope, $timeout, error.data.message);
	});
	
	
	$scope.onTakeOrderModalOpening = function(order)
	{
		$scope.orderToTake = order;
		ordersFactory.getCartItems(order.id).then(function(data)
		{
			$scope.totalPrice = order.totalPrice;
			$scope.items = data.data;
			for (let index in $scope.items)
			{
				const discount = $scope.items[index].article.discount;
				const price = (discount && discount > 0) ? $scope.items[index].article.price * (100 - discount) / 100 : $scope.items[index].article.price;
				$scope.items[index].discountPrice = price;
			}
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	$scope.takeOrder = function()
	{
		deliverersFactory.takeOverOrder($scope.user.id, $scope.orderToTake).then(function(data)
		{
			getDelivererData($scope.user.id);
			$('#takeOrderModal').modal('hide');
			
			displaySuccessMessage($scope, $timeout, 'You have taken order successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	$scope.deliverOrder = function()
	{
		deliverersFactory.deliverOrder($scope.user.id, $scope.deliveryInProgress).then(function(data)
		{
			getDelivererData($scope.user.id);
			
			displaySuccessMessage($scope, $timeout, 'Order is delivered successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	$scope.cancelOrder = function()
	{
		deliverersFactory.cancelOrder($scope.user.id, $scope.deliveryInProgress).then(function(data)
		{
			getDelivererData($scope.user.id);
			
			displaySuccessMessage($scope, $timeout, 'Order is canceled successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	$scope.onAddArticleCategoryModalOpening = function()
	{
		$scope.title = 'New article category';
		$scope.adding = true;
	}
	
	$scope.addCategory = function()
	{
		$scope.submitted = true;
		
		if ($scope.articleCategoryForm.$invalid)
		{
			return;
		}
		
		articleCategoriesFactory.add($scope.newCategory).then(function(data)
		{
			getAdministratorData($scope.user.id);
			
			$('#articleCategoryModal').modal('hide');
			
			displaySuccessMessage($scope, $timeout, 'Article category \'' + data.data.name + '\' is added successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	$scope.onEditArticleCategoryModalOpening = function(category)
	{
		$scope.title = 'Update article category';
		$scope.adding = false;
		
		$scope.newCategory = {};
		$scope.newCategory.id = category.id;
		$scope.newCategory.name = category.name;		
	}
	
	$scope.editCategory = function()
	{
		$scope.submitted = true;
		
		if ($scope.articleCategoryForm.$invalid)
		{
			return;
		}
		
		articleCategoriesFactory.update($scope.newCategory).then(function(data)
		{
			getAdministratorData($scope.user.id);
			
			$('#articleCategoryModal').modal('hide');
			displaySuccessMessage($scope, $timeout, 'Article category \'' + data.data.name + '\' is updated successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	$scope.deleteCategory = function(category)
	{
		articlesFactory.getArticles().then(function(data)
		{
			let categoryUsed = false;
			for (let article of data.data)
			{
				if (article.category.id == category.id)
				{
					categoryUsed = true;
					break;
				}
			}
			
			if (categoryUsed)
			{
				$scope.failAlertMessage = 'Category \'' + category.name + '\' cannot be deleted because it is used by one or more articles.';
				$scope.failAlertVisibility = true;
				
				$timeout(function ()
				{
					$scope.failAlertVisibility = false;
				}, 5000);
			}
			else
			{
				articleCategoriesFactory.delete(category.id).then(function(data)
				{
					getAdministratorData($scope.user.id);
					
					displaySuccessMessage($scope, $timeout, 'Article category \'' + category.name + '\' is removed successfully.');
				})
				.catch(function(error) 
				{
					displayFailureMessage($scope, $timeout, error.data.message);
				});
			}	
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	$scope.addDeliverer = function()
	{
		$scope.submitted = true;
		
		if ($scope.delivererForm.$invalid)
		{
			return;
		}
		
		deliverersFactory.add($scope.newDeliverer).then(function(data)
		{
			getAdministratorData($scope.user.id);
			
			$('#delivererModal').modal('hide');
			
			displaySuccessMessage($scope, $timeout, 'Deliverer \'' + data.data.username + '\' is added successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	$scope.onEditDelivererModalOpening = function(deliverer)
	{	
		$scope.deliverer = {};
		$scope.deliverer.id = deliverer.id;
		$scope.deliverer.username = deliverer.username;
		$scope.deliverer.password = deliverer.password;
		$scope.deliverer.firstname = deliverer.firstname;
		$scope.deliverer.lastname = deliverer.lastname;
		$scope.deliverer.role = deliverer.role;
		$scope.deliverer.phoneNumber = deliverer.phoneNumber;
		$scope.deliverer.email = deliverer.email;
		$scope.deliverer.address = deliverer.address;
	}
	
	$scope.editDeliverer = function()
	{
		$scope.submitted = true;
		
		if ($scope.delivererEditForm.$invalid)
		{
			return;
		}
		
		deliverersFactory.update($scope.deliverer).then(function(data)
		{
			getAdministratorData($scope.user.id);
			
			$('#delivererEditModal').modal('hide');
			displaySuccessMessage($scope, $timeout, 'Deliverer \'' + data.data.username + '\' is updated successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	$scope.deleteDeliverer = function(deliverer)
	{	
		deliverersFactory.delete(deliverer.id).then(function(data)
		{
			getAdministratorData($scope.user.id);
			
			displaySuccessMessage($scope, $timeout, 'Deliverer \'' + deliverer.username + '\' is removed successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	$scope.changeRole = function(user)
	{
		const roleChange = {"id" : user.id, "oldRole" : user.originalRole, "newRole" : user.role};
		
		usersFactory.changeRole(roleChange).then(function(data)
		{
			getAdministratorData($scope.user.id);
			
			displaySuccessMessage($scope, $timeout, 'Role of the user \'' + user.username + '\' is changed successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
});