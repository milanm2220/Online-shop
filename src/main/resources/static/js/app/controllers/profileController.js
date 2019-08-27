/**
 * Controller for displaying user profile info
 */

webShopApp.controller('profileController', function($scope, $window, $timeout, usersFactory, customersFactory) 
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
	
	usersFactory.getLoggedInUser().then(function(data) 
	{
		$scope.user = data.data;
		
		if ($scope.user.role == 'CUSTOMER')
		{
			customersFactory.getFavouriteArticles($scope.user.id).then(function(data)
			{
				$scope.articles = data.data;
			})
			.catch(function(error) 
			{
				displayFailureMessage(error);
			});
		}
	})
	.catch(function(error) 
	{
		displayFailureMessage(error);
	});
});