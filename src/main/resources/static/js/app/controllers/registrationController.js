/**
 * Contoller for handling customer registration
 */

webShopApp.controller('registrationController', function ($scope, $location, $timeout, usersFactory) 
{   
	$scope.newUser = {};
	
	$scope.registerUser = function() 
	{
		$scope.submitted = true;
		
		if ($scope.regForm.$invalid) { return; }
					
		usersFactory.registerUser($scope.newUser).then(function(data)
		{
			$('#registrationModal').modal('hide');
			$location.path('/');
			
			$scope.successAlertMessage = 'You are signed up successfully.';
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