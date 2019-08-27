/**
 * Controller for manipulating orders
 */

webShopApp.controller('orderController', function ($scope, $timeout, ordersFactory) 
{  
	$scope.putInCart = function()
	{
		$scope.itemToPutInCart.amount = $scope.itemAmount;
		$('#cartModal').modal('hide');
		console.log($scope.itemToPutInCart);
	}
});