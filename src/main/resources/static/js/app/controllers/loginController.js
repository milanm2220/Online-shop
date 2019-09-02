/**
 * Controller for handling user logout
 */

webShopApp.controller('loginController', function ($scope, $location, $timeout, $window, usersFactory) 
{   
	$scope.credentials = {};
	
	$scope.login = function() 
	{					
		usersFactory.login($scope.credentials).then(function(data)
		{
			$window.localStorage.setItem(ID_KEY, data.data.id);
			$window.localStorage.setItem(USERNAME_KEY, data.data.username);
			$window.localStorage.setItem(ROLE_KEY, data.data.role);
		    $scope.loggedInUserInfo.username = data.data.username;
		    $scope.loggedInUserInfo.role = data.data.role;
			
			$('#loginModal').modal('hide');
			$location.path('/');
			
			displaySuccessMessage($scope, $timeout, 'You are logged in successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	};
});