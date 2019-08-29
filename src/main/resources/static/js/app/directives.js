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

webShopApp.directive('cart', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/cart.html'
    };
});

webShopApp.directive('cartModal', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/cartModal.html'
    };
});

webShopApp.directive('favouriteArticlesRepeater', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/favouriteArticlesRepeater.html'
    };
});

webShopApp.directive('orderModal', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/orderModal.html'
    };
});

webShopApp.directive('ordersRepeater', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/ordersRepeater.html'
    };
});

webShopApp.directive('takeOrderModal', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/takeOrderModal.html'
    };
});

webShopApp.directive('articleModal', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/articleModal.html'
    };
});

webShopApp.directive('profileAdministrator', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/profileAdministrator.html'
    };
});

webShopApp.directive('profileCustomer', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/profileCustomer.html'
    };
});

webShopApp.directive('profileDeliverer', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/profileDeliverer.html'
    };
});

webShopApp.directive('articleCategoryModal', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/articleCategoryModal.html'
    };
});

webShopApp.directive('delivererModal', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/delivererModal.html'
    };
});

webShopApp.directive('delivererEditModal', function() 
{
    return {
        restrict: 'E',
        templateUrl: '../directives/delivererEditModal.html'
    };
});