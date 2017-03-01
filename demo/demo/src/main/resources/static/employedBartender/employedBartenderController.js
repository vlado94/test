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
			
			/*employedBartenderService.readyDrinks().then(
				function(response){
					$scope.readyDrinks = response.data;
				}
			);*/
			
			employedBartenderService.employedBartenders().then(
					function(response){
						$scope.employedBartenders = response.data;
					}
				)
		}
		
		$scope.ready = function(drinkOrder){
			employedBartenderService.ready(drinkOrder.id).then(
				function(response) {
					if(response.data == ""){
						alert("Order is changed you can't make drinks ready at the moment");
						findAll();
					} else {
						$scope.drinkOrders.splice($scope.drinkOrders.indexOf(drinkOrder),1);
						findAll();
					}
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
		
		$scope.changedShift = function(bartender) {
			
		    var today = Date.now();
		    var tomorrow = new Date(Date.now() + 86400000 * 1);
			var step = 2;
			var datesArr = [];
			var temp = 0;
			if(bartender.defaultShift != "First") 
				temp = 1;
			else
				temp = 0;
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
}]);