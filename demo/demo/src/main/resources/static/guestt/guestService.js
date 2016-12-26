var services = angular.module('guest.services', ['ngResource']);

var baseUrl = 'http://localhost\\:8080';

services.service('guestService', ['$http', function($http){
	
	this.checkRights = function(){
		return $http.get("/guest/checkRights");
	}
	
	this.getLoggedUser = function(){
		return $http.get("/commonController/getLoggedUser");
	}
	
	this.findFriends = function(input){
		return $http.get("/guest/findByFirstAndLastName/"+input);
	}
	
	this.updateGuestProfile = function(guest){
		return $http.put("/guest/"+guest.id,guest);
	}
	
	this.sendRequest = function(id){
		return $http.get("friends/addFriend/"+id);
	}
	
	this.listFriends = function(){
		return $http.get("/friends/list");
	}
	
	this.findAllRecivedPendingRequests = function(){
		return $http.get("/friends/recivedRequests");
	}
	
	this.acceptFriendRequest = function(id){
		return $http.get("/friends/acceptRequest/"+id);
	}
	
	this.rejectFriendRequest = function(id){
		return $http.get("/friends/rejectRequest/"+id);
	}
	
	this.unfriend = function(id){
		return $http.get("/friends/unfriend/"+id);
	}
	
	
}]);