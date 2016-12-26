var app = angular.module('systemManager.controllers', []);

app.controller('systemManagerController', ['$scope','systemManagerService','$location',
	function ($scope, systemManagerService, $location) {
		function checkRights() {
			systemManagerService.checkRights().then(
				function (response) {
					if(response.data === 'true')
						findAll();
					else {
					    $location.path('login');
					    alert("Access denied!");
				    }
				}
			);
		}
		checkRights();
		
		function findAll() {
			systemManagerService.findSystemManager().then(
				function (response) {
					$scope.systemManager = response.data;
				}
		    );
			
			systemManagerService.findAllRestaurantManagers().then(
				function (response) {
					$scope.restaurantManagers = response.data;
				}
			);
			
			systemManagerService.findAllRestaurant().then(
					function (response) {
						$scope.restaurants = response.data;
					}
			);
		}
		
		$scope.findAllFreeRestaurantManagers = function () {            
			systemManagerService.findAllFreeRestaurantManagers().then(
				function (response) {
					$scope.freeRestaurantManagers = response.data;
				}
			); 	
		};
		
		$scope.saveManager = function () {            
			systemManagerService.saveRestaurantManager($scope.restaurantManager).then(
				function (response) {
                    alert("Uspesno dodat.");
                    findAll();
                    $scope.state = undefined;
                    $location.path('loggedIn/systemManager/list');
                },
                function (response) {
                    alert("Greska pri dodavanju.");
                }
            ); 	
		};
		
		$scope.saveRestaurant = function () {            
			systemManagerService.saveRestaurant($scope.restaurant).then(
				function (response) {
                    alert("Uspesno dodat.");
                    findAll();
                    $scope.state = undefined;
                    $location.path('loggedIn/systemManager/list');
                },
                function (response) {
                    alert("Greska pri dodavanju.");
                }
            ); 	
		};
		
		$scope.update = function() {
			systemManagerService.updateSystemMangerProfile($scope.systemManager).then(
				function (response) {
                    alert("Successfully change.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/systemManager/list');
                },
                function (response) {
                    alert("Error in changing.");
                }
			);
		}
}]);