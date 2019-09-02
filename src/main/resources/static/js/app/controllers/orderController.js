/**
 * Controller for manipulating orders
 */

webShopApp.controller('orderController', function ($scope, $timeout, $window, customersFactory, ordersFactory) 
{  
	$scope.totalPrice = 0;
	
	$scope.$watch(function()
	{
	  return $window.localStorage.getItem(ROLE_KEY);
	},
	function(newCodes, oldCodes)
	{
		getBonusPoints();
		getCart();
	});
	
	const getBonusPoints = function()
	{
		if ($window.localStorage.getItem(ROLE_KEY) == 'CUSTOMER')
		{
			customersFactory.getBonusPoints($window.localStorage.getItem(ID_KEY)).then(function(data)
			{
				$scope.bonusPoints = data.data;
				$scope.bonusPointsPlusOne = parseInt($scope.bonusPoints, 10) + 1;
			})
			.catch(function(error) 
			{
				displayFailureMessage($scope, $timeout, error.data.message);
			});
		}		
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
				displayFailureMessage($scope, $timeout, error.data.message);
			});
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
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
			const price = (discount) ? $scope.itemToPutInCart.article.price * (100 - discount) / 100 : $scope.itemToPutInCart.article.price;
			data.data.discountPrice = price;
			$scope.items.push(data.data);
			$scope.totalPrice += price * $scope.itemToPutInCart.amount;
			displaySuccessMessage($scope, $timeout, 'Article \'' + $scope.itemToPutInCart.article.name + '\' is added to the cart.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	}
	
	$scope.dropItemFromCart = function(index)
	{
		const itemToDrop = $scope.items[index];
		customersFactory.dropItemFromCart(itemToDrop, $window.localStorage.getItem(ID_KEY)).then(function(data)
		{
			$scope.items.splice(index, 1);
			const discount = itemToDrop.article.discount;
			const price = (discount) ? itemToDrop.article.price * (100 - discount) / 100 : itemToDrop.article.price;
			$scope.totalPrice -= price * itemToDrop.amount;
			
			displaySuccessMessage($scope, $timeout, 'Article \'' + $scope.itemToPutInCart.article.name + '\' is dropped from the cart.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
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
			
			displaySuccessMessage($scope, $timeout, 'Order is completed successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	};
});