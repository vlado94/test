var app = angular.module('employedWaiter.controllers', []);

app.controller('employedWaiterController', [ '$scope', 'employedWaiterService','$location',
	function($scope, employedWaiterService, $location) {
		function checkRights(){
			employedWaiterService.checkRights().then(
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
			employedWaiterService.findWaiter().then(
				function(response){
					$scope.waiter = response.data;
				}
			);
			
			
			employedWaiterService.readyDrinks().then(
				function(response){
					$scope.readyDrinks = response.data;
				}
			);
			
			employedWaiterService.readyFood().then(
				function(response){
					$scope.readyFood = response.data;
				}
			);
			
			employedWaiterService.readyOrders().then(
				function(response){
					$scope.orders = response.data;
				}
			);
		}
		
		$scope.makeBill = function(order){
			employedCookService.makeBill(order.id).then(
				function(response){
					$scope.orders.splice($scope.orders.indexOf(order),1);
				},
				function(response){
					alert("Error while signal");
				}
			);
		}		
}]);