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
					$scope.orders = response.data;
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
					$scope.orders.splice($scope.orders.indexOf(foodOrder),1);
					findAll();
				},
				function(response){
					alert("Error while signal");
				}
			);
		}
		
		$scope.ready = function(order){
			employedCookService.ready(order.id).then(
				function(response){
					$scope.receivedFood.splice($scope.receivedFood.indexOf(order),1);
					findAll();
				},
				function(response){
					alert("Error while signal");
				}
			);
		}
		
		$scope.changeProfile = function(){
			employedCookService.changeProfile($scope.cook.id,$scope.cook).then(
				function(response){
					alert("successfully changed profile");
					$scope.state = undefined;
					findAll();
					$location.path('loggedIn/cook/home');
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		
		$scope.changePassword = function(){
			employedCookService.changePassword($scope.cook.id,$scope.cook).then(
				function(response){
					alert("successfully changed password");
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