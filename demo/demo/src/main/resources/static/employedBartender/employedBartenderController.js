var app = angular.module('employedBartender.controllers', []);

app.controller('employedBartenderController', [ '$scope', 'employedBartenderService','$location',
	function($scope, employedBartenderService, $location) {
		function checkRights(){
			employedBartenderService.checkRights().then(
				function(response){
					if(response.data === 'true')
						findAll();
					else{
						$location.path('login')
						alert("Access denied");
					}
				}
			);
		}
		checkRights();
		
		function findAll(){
			employedBartenderService.findBartender().then(
				function(response){
					$scope.bartender = response.data;
				}
			);
			
			employedBartenderService.findAllOrdres().then(
				function(response){
					$scope.drinkOrders = response.data;
				}
			);
			
			employedBartenderService.readyDrinks().then(
				function(response){
					$scope.readyDrinks = response.data;
				}
			);
		}
		
		$scope.ready = function(drinkOrder){
			employedBartenderService.ready(drinkOrder.id).then(
				function(response) {
					$scope.drinkOrders.splice($scope.drinkOrders.indexOf(drinkOrder),1);
					findAll();
				},
				function(response){
					alert("Error while signal")
				}
			);
		}
		
		$scope.changeProfile = function(){
			employedBartenderService.changeProfile($scope.bartender).then(
				function(response){
					alert("successfully changed profile");
					$scope.state = undefined;
					findAll();
					$location.path('loggedIn/bartender/home');
				},
				function(response){
					alert("Error in changing");
				}
			);
		}	
		
		$scope.changePassword = function(){
			employedBartenderService.changePassword($scope.bartender.id,$scope.bartender).then(
				function(response){
					alert("successfully changed password");
					$scope.state = undefined;
					findAll();
					$location.path('loggedIn/bartender/home');
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
}]);