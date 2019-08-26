/**
 * Adding directives to the main angular module
 */

webShopApp.directive('alerts', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/alerts.html'
    };
});

webShopApp.directive('registrationModal', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/registrationModal.html'
    };
});

webShopApp.directive('loginModal', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/loginModal.html'
    };
});

webShopApp.directive('articlesRepeater', function() 
		{
		    return {
		        restrict: 'E',
		        templateUrl: '../directives/articlesRepeater.html'
		    };
		});