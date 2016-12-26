var app = angular.module('restaurantManager.controllers', []);

app.controller('restaurantManagerController', ['$scope','restaurantManagerService', '$location',
	function ($scope, restaurantManagerService, $location) {
		function checkRights() {
			restaurantManagerService.checkRights().then(
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
			restaurantManagerService.findRestaurant().then(
				function (response) {
					$scope.restaurant = response.data;
					$scope.waiters = response.data.waiters;
					$scope.cooks = response.data.cooks;
					$scope.bartenders = response.data.bartenders;
					$scope.bidders = response.data.bidders;
				}
	        );
		}
		
		$scope.saveDrink = function() {
			restaurantManagerService.saveDrink($scope.drink).then(
				function (response) {
                    alert("Successfully added.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/restaurantManager/info');
                },
                function (response) {
                    alert("Error in adding.");
                }
			);
		}
		
		$scope.saveDish = function() {
			restaurantManagerService.saveDish($scope.dish).then(
				function (response) {
                    alert("Successfully added.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/restaurantManager/info');
                },
                function (response) {
                    alert("Error in adding.");
                }
			);
		}
		
		$scope.update = function() {
			restaurantManagerService.updateMangerProfile($scope.restaurant.restaurantManager).then(
				function (response) {
                    alert("Successfully change.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/restaurantManager/info');
                },
                function (response) {
                    alert("Error in changing.");
                }
			);
		}
		
		$scope.saveBidder = function() {
			restaurantManagerService.saveBidder($scope.bidder).then(
				function (response) {
                    alert("Successfully added.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/restaurantManager/info');
                },
                function (response) {
                    alert("Error in adding.");
                }
			);
		}
		
		$scope.saveEmployed = function() {
			//$scope.drink.restaurant = $scope.restaurant;
			if($scope.employedType == 'Waiter') {
				restaurantManagerService.saveWaiter($scope.employed).then(
					function (response) {
	                    alert("Successfully added.");
	                    $scope.state = undefined;
	                    findAll();
	                    $location.path('loggedIn/restaurantManager/info');
	                },
	                function (response) {
	                    alert("Error in adding.");
	                }
				);
			}
			else if($scope.employedType == 'Cook') {
				restaurantManagerService.saveCook($scope.employed).then(
					function (response) {
						alert("Successfully added.");
		                $scope.state = undefined;
		                findAll();
		                $location.path('loggedIn/restaurantManager/info');
		            },
		            function (response) {
		            	alert("Error in adding.");
		            }
				);
			}
			else if($scope.employedType == 'Bartender') {
				restaurantManagerService.saveBartender($scope.employed).then(
					function (response) {
						alert("Successfully added.");
		                $scope.state = undefined;
		                findAll();
		                $location.path('loggedIn/restaurantManager/info');
					},
		            function (response) {
						alert("Error in adding.");
		            }
				);
			}
		}		
}]);