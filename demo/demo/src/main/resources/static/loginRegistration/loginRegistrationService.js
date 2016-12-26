var services = angular.module('loginRegistration.services', ['ngResource']);

var baseUrl = 'http://localhost\\:8080';

services.service('loginRegistrationService', ['$http', function($http){
	
	this.logIn = function(user){
		return $http.post("/commonController/logIn",user);
	}
	
	this.logOut = function(){
		return $http.get("/commonController/logOut");
	}
	
	this.save = function(guest){
		return $http.post("/commonController/registration",guest);
	}
	
	this.firstLogin = function(id,user){
		return $http.put("/commonController/"+id,user);
	}
}]);