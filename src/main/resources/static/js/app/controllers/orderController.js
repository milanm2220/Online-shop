/**
 * Controller for manipulating orders
 */

webShopApp.controller('orderController', function ($scope, $timeout, $window, customersFactory, ordersFactory) 
{  
	const displayFailureMessage = function(error)
	{
		$scope.failAlertMessage = error.data.message;
		$scope.failAlertVisibility = true;
		
		$timeout(function ()
		{
			$scope.failAlertVisibility = false;
		}, 3000);
	}
	
	$scope.totalPrice = 0;
	
	$scope.$watch(function()
	{
	  return $window.localStorage.getItem(ROLE_KEY);
	},
	function(newCodes, oldCodes)
	{
		getCart();
	});
		
	const getCart = function()
	{
		if (!$window.localStorage.getItem(ID_KEY))
		{
			return;
		}
		
		customersFactory.getCart($window.localStorage.getItem(ID_KEY)).then(function(data) 
		{
			if (!data.data.id)
			{
				return;
			}
			ordersFactory.getCartItems(data.data.id).then(function(data)
			{
				$scope.items = data.data;
				$scope.totalPrice = 0;
				for (let item of $scope.items)
				{
					$scope.totalPrice += item.article.price * item.amount;
				}
			})
			.catch(function(error) 
			{
				displayFailureMessage(error);
			});
		})
		.catch(function(error) 
		{
			displayFailureMessage(error);
		});
	}
	
	getCart();
	
	$scope.putInCart = function()
	{
		$scope.itemToPutInCart.amount = $scope.itemAmount;
		$('#cartModal').modal('hide');
		
		if (!$scope.items)
		{
			$scope.items = [];
		}
		
		customersFactory.addItemToCart($scope.itemToPutInCart, $window.localStorage.getItem(ID_KEY)).then(function(data) 
		{
			$scope.items.push($scope.itemToPutInCart);
			$scope.totalPrice += $scope.itemToPutInCart.article.price * $scope.itemToPutInCart.amount;
			$scope.successAlertMessage = 'Article \'' + $scope.itemToPutInCart.article.name + '\' is added to the cart.';
			$scope.successAlertVisibility = true;
			
			$timeout(function ()
			{
				$scope.successAlertVisibility = false;
			}, 3000);
		})
		.catch(function(error) 
		{
			displayFailureMessage(error);
		});
	}
	
	$scope.dropItemFromCart = function(index)
	{
		$scope.itemToDrop = $scope.items[index];
		ordersFactory.dromItemFromCart(itemIndex).success(function(data)
		{
			$scope.items.splice(itemIndex, 1);
			
			$scope.totalPrice -= $scope.itemToDrop.article.price * $scope.itemToDrop.amount;
			
			$scope.successAlertMessage = 'Article \'' + $scope.itemToDrop.article.name + '\' is removed from the cart.';
			$scope.successAlertVisibility = true;
			
			$timeout(function ()
			{
				$scope.successAlertVisibility = false;
			}, 1500);
		});
	}
});