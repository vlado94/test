var app = angular.module('employedCook.controllers',[]);

app.controller('employedCookController',['$scope','$filter','employedCookService','$location',
	function($scope,$filter, employedCookService, $location){
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
			
			employedCookService.employedCooks().then(
				function(response){
					$scope.employedCooks = response.data;
				}
			)
			
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
			
		}
		
		$scope.received = function(foodOrder){
			employedCookService.received(foodOrder.id, foodOrder.changeVersion).then(
				function(response){
					if(response.data == ""){
						alert("Order is changed you can't receive it now");
						findAll();
					} else {
						$scope.orders.splice($scope.orders.indexOf(foodOrder),1);
						findAll();
					}
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
		
		$scope.changedShift = function(cook) {
			employedCookService.changedShiftDate(cook.id).then(
				function(response){
					var today = Date.now();
				    var tomorrow = new Date(Date.now() + 86400000 * 1);
					var step = 2;
					var datesArr = [];
					var temp = 0;
					if(cook.defaultShift != "First") 
						temp = 1;
					else
						temp = 0;
					
					dates = response.data;
					for(var i = 0;i<300;i++) {
						day = new Date(Date.now() +temp * 86400000 + 86400000 *  i*step);
												
						datesArr.push(day);
						
					}
					
					
					$('#date').multiDatesPicker('destroy');
					$('#date').multiDatesPicker({
				        numberOfMonths: 1,
				        addDates: datesArr
					});
					if(temp == 1)
						$('#date').multiDatesPicker('toggleDate', new Date());
				}
			)
			    
		}
		
}]);