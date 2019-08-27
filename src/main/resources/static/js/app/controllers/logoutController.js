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
			
			$scope.successAlertMessage = 'You are logged out successfully.';
			$scope.successAlertVisibility = true;
			
			$timeout(function ()
			{
				$scope.successAlertVisibility = false;
			}, 3000);
		})
		.catch(function(error) 
		{
			$scope.failAlertMessage = error.data.message;
			$scope.failAlertVisibility = true;
			
			$timeout(function ()
			{
				$scope.failAlertVisibility = false;
			}, 3000);
		});
	};
});