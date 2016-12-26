var app = angular.module('bidder.controllers', []);
 
app.controller('bidderController', ['$scope','bidderService', '$location',
  	function ($scope, bidderService, $location) {
	
		function checkRights() {
			bidderService.checkRights().then(
				function (response) {
					if(response.data === 'true')
						findBidder();
					else {
					    $location.path('login');
					    alert("Access denied!");
				    }
				}
			);
		}
		checkRights();
	
		function findBidder() {
			bidderService.findBidder().then(
				function (response) {
					$scope.bidder = response.data;
				}
	        );
		}
	}
]);