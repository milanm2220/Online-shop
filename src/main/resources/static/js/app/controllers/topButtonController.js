/**
 * Contoller for button that is used to scroll to the top of the page
 */

webShopApp.controller('topButtonController', function($scope, $window, $timeout) 
{
	$scope.scrollToTop = function()
	{
		var top  = window.pageYOffset || document.documentElement.scrollTop,
	    left = window.pageXOffset || document.documentElement.scrollLeft;	
		
		$window.scrollTo(left, 0);
	};
	
	$window.onscroll = function()
	{	
		if (document.getElementById("topBtnId") != null)
		{		
			if ($window.scrollY > 120)
			{
				document.getElementById("topBtnId").style.display = "block";
			}
			else
			{
				document.getElementById("topBtnId").style.display = "none";
			}
		}
	}
});