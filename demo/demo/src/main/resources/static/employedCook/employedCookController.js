var app = angular.module('employedCook.controllers',[]);

app.controller('employedCookController',['$scope','employedCookService','$location',
	function($scope, employedCookService, $location){
		function checkRights(){
			employedCookService.checkRights().then(
				function(response){
					if(response.data === 'true')
						findAll();
					else{
						location.path('login');
						alert("Access denied");
					}
				}
			);
		}
		checkRights();
		
		function findAll(){
			employedCookService.findCook().then(
				function(response){
					$scope.cook = response.data;
				}
			);
			
			employedCookService.findAllOrders().then(
				function(response){
					$scope.foodOrders = response.data;
				}
			);
			
			employedCookService.receivedFood().then(
				function(response){
					$scope.receivedFood = response.data;
				}
			);
			
			employedCookService.readyFood().then(
				function(response){
					$scope.readyFood = response.data;
				}
			);
		}
		
		$scope.received = function(foodOrder){
			employedCookService.received(foodOrder.id).then(
				function(response){
					$scope.foodOrders.splice($scope.foodOrders.indexOf(foodOrder),1);
				},
				function(response){
					alert("Error while signal");
				}
			);
		}
		
		$scope.ready = function(receivedDish){
			employedCookService.ready(receivedDish.id).then(
				function(response){
					$scope.receivedFood.splice($scope.receivedFood.indexOf(receivedDish),1);
				},
				function(response){
					alert("Error while signal");
				}
			);
		}
		
		$scope.changeProfile = function(){
			employedCookService.changeProfile($scope.cook.id,$scope.cook).then(
				function(response){
					alert("successfully added");
					$scope.state = undefined;
					findAll();
					$location.path('loggedIn/cook/home');
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
}]);