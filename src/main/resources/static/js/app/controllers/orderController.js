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
	
	if ($window.localStorage.getItem(ID_KEY) && $window.localStorage.getItem(ROLE_KEY) && $window.localStorage.getItem(ROLE_KEY) == 'CUSTOMER')
	{
		customersFactory.getBonusPoints($window.localStorage.getItem(ID_KEY)).then(function(data)
		{
			$scope.bonusPoints = data.data;
			$scope.bonusPointsPlusOne = parseInt($scope.bonusPoints, 10) + 1;
		})
		.catch(function(error) 
		{
			displayFailureMessage(error);
		});
	}
	
	const getCart = function()
	{
		if (!$window.localStorage.getItem(ID_KEY) || $window.localStorage.getItem(ROLE_KEY) != 'CUSTOMER')
		{
			return;
		}
		
		customersFactory.getCart($window.localStorage.getItem(ID_KEY)).then(function(data) 
		{
			$scope.items = [];
			$scope.totalPrice = 0;
			
			if (!data.data.id)
			{
				return;
			}
			
			ordersFactory.getCartItems(data.data.id).then(function(data)
			{
				$scope.items = data.data;
				for (let index in $scope.items)
				{
					const discount = $scope.items[index].article.discount;
					const price = (discount && discount > 0) ? $scope.items[index].article.price * (100 - discount) / 100 : $scope.items[index].article.price;
					$scope.items[index].discountPrice = price;
					$scope.totalPrice += price * $scope.items[index].amount;
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
	
	$scope.putInCart = function()
	{
		$scope.itemToPutInCart.amount = $scope.itemAmount;
		$('#cartModal').modal('hide');
		
		$("#cartModal").on("hidden.bs.modal", function(){
			$scope.itemAmount = 1;
		});
		
		if (!$scope.items)
		{
			$scope.items = [];
		}
		
		customersFactory.addItemToCart($scope.itemToPutInCart, $window.localStorage.getItem(ID_KEY)).then(function(data) 
		{
			const discount = $scope.itemToPutInCart.article.discount;
			const price = (discount && discount > 0) ? $scope.itemToPutInCart.article.price * (100 - discount) / 100 : $scope.itemToPutInCart.article.price;
			data.data.discountPrice = price;
			$scope.items.push(data.data);
			$scope.totalPrice += price * $scope.itemToPutInCart.amount;
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
		const itemToDrop = $scope.items[index];
		customersFactory.dropItemFromCart(itemToDrop, $window.localStorage.getItem(ID_KEY)).then(function(data)
		{
			$scope.items.splice(index, 1);
			const discount = itemToDrop.article.discount;
			const price = (discount && discount > 0) ? itemToDrop.article.price * (100 - discount) / 100 : itemToDrop.article.price;
			$scope.totalPrice -= itemToDrop.article.price * itemToDrop.amount;
			
			$scope.successAlertMessage = 'Article \'' + itemToDrop.article.name + '\' is dropped from the cart.';
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
	
	$scope.pointsChanged = function(points)
	{
		$scope.pointsToUseValue = points;
	}
	
	$scope.order = function()
	{
		customersFactory.order($window.localStorage.getItem(ID_KEY), $scope.pointsToUseValue).then(function(data)
		{	
			getCart();
			$('#orderModal').modal('hide');
			$scope.pointsToUseValue = 0;
			
			$scope.successAlertMessage = 'Order is completed successfully.';
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
	};
});