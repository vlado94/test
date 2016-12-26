var app = angular.module('guest.controllers', []);
 
app.controller('guestController', ['$scope','$window','guestService', '$location',
  	function ($scope,$window, guestService, $location) {
		function checkRights() {			
			guestService.checkRights().then(
				function (response) {
					if(response.data === 'true'){
						
					}
					else {
						$location.path('login');
					}
				}
			);
		}
		checkRights();	
	
		$scope.getLoggedUser = function() {
			guestService.getLoggedUser().then(
				function (response) {
					$scope.loggedUser = response.data;
	            }		
			)
		}
		
		$scope.findFriends = function(){
			if($scope.inputStr !== '') {
				guestService.findFriends($scope.inputStr).then(
					function(response){
						$scope.guests = response.data;
					}
				)
			}
		}
		
		$scope.update = function() {
			guestService.updateGuestProfile($scope.loggedUser).then(
				function (response) {
                    alert("Successfully change.");
                    $scope.state = undefined;
                    $location.path('loggedIn/guest/profile');
                },
                function (response) {
                    alert("Error in changing.");
                }
			);
		}
		
		$scope.sendRequest = function(id) {
			guestService.sendRequest(id);
			$window.location.reload();
		}
		
		$scope.listFriends = function(){
			guestService.listFriends().then(
				function (response) {
					$scope.friends = [];	//pravim niz trojki: Guest za prikaz, status(pending, friends, rejected), id prijateljstva
					angular.forEach(response.data, function(value, key){
						if(value.friendSendRequest.id == $scope.loggedUser.id){	
							var guestAndStatus = {guest: value.friendReciveRequest, status: value.status, id:value.id };
							$scope.friends.push(guestAndStatus);
						}
						else{
							if(value.status == "Friends"){	//jer ne zelim da stoji pending ili rejected status kod primaoca zahteva
								var guestAndStatus = {guest: value.friendSendRequest, status: value.status,  id:value.id };
								$scope.friends.push(guestAndStatus);
							}
						}
					});
		        }		
			);
		}
		
		$scope.getRequests = function(){
			guestService.findAllRecivedPendingRequests().then(
					function (response) {
						$scope.recivedPending = response.data;
					}
			)
		}
		
		$scope.acceptRequest = function(id){
			guestService.acceptFriendRequest(id).then(
					function() {
						alert("Friend request acccepted.");
						$window.location.reload();
					}
			)
		}
		
		$scope.rejectRequest = function(id){
			guestService.rejectFriendRequest(id).then(
					function() {
						alert("Friend request rejected.");
						$window.location.reload();
					}
			)
		}
		
		$scope.unfriend = function(id){
			guestService.unfriend(id).then(
			function(){
				alert("Unfriend Successful.");
				$window.location.reload();
			})
		}
		
		
		
}]);