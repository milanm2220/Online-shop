/**
 * Controller for handling user logout
 */

webShopApp.controller('logoutController', function ($scope, $location, $timeout, $window, usersFactory) 
{   
	$scope.logout = function() 
	{					
		usersFactory.logout().then(function()
		{
			$window.localStorage.removeItem(USERNAME_KEY);
			$window.localStorage.removeItem(ROLE_KEY);
			$window.localStorage.removeItem(ID_KEY);
		    $scope.loggedInUserInfo.username = undefined;
		    $scope.loggedInUserInfo.role = undefined;
		    
			$location.path('/');
		
			displaySuccessMessage($scope, $timeout, 'You are logged out successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	};
});