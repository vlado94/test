var app = angular.module('bossManager.controllers', []);

app.controller('bossManagerController', ['$scope','bossManagerService', '$location',
	function ($scope, bossManagerService, $location) {
		function checkRights() {			
			bossManagerService.checkRights().then(
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
			bossManagerService.findAllSystemManagers().then(
				function (response) {
					$scope.systemMenagers = response.data;
				}
			);
			
			bossManagerService.findBossManager().then(
					function (response) {
						$scope.bossManager = response.data;
					}
			    );
		}
		
		$scope.saveSystemManager = function () {            
			$scope.systemManager.registrated = "0";
			bossManagerService.save($scope.systemManager).then(
				function (response) {
                    alert("Uspesno dodat.");
                	findAll();
					$scope.state = undefined;
                    $location.path('loggedIn/bossManager/list');
                },
                function (response) {
                    alert("Greska pri dodavanju.");
                }
            ); 	
		};
		
		$scope.update = function() {
			bossManagerService.updateBossMangerProfile($scope.bossManager).then(
				function (response) {
                    alert("Successfully change.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/boss/list');
                },
                function (response) {
                    alert("Error in changing.");
                }
			);
		}
}]);