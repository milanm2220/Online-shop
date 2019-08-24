/**
 * Controller for buttons located in the nabvar
 */

webShopApp.controller('navbarController', function ($scope, $location, usersFactory) 
{   
    $scope.isActive = function (viewLocation) 
    { 
        return viewLocation === $location.path();
    };
});