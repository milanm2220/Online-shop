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
			
			displaySuccessMessage($scope, $timeout, 'You are signed up successfully.');
		})
		.catch(function(error) 
		{
			displayFailureMessage($scope, $timeout, error.data.message);
		});
	};
});