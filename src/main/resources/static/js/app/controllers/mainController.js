/**
 * Controller for buttons located in the nabvar
 */

webShopApp.controller('mainController', function ($scope, $location, $window, usersFactory) 
{   
	$scope.loggedInUserInfo = {};
    $scope.loggedInUserInfo.username = $window.localStorage.getItem(USERNAME_KEY);
    $scope.loggedInUserInfo.role = $window.localStorage.getItem(ROLE_KEY);
    
    $scope.isActive = function (viewLocation) 
    { 
        return viewLocation === $location.path();
    };
    
    $scope.showPassword = function (id) 
	{
	    usersFactory.showPassword(document.getElementById(id));
	};
});