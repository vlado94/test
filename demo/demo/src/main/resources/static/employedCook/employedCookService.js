var services = angular.module('employedCook.services',['ngResource']);

var baseUrl = 'http://localhost\\:8080';

services.service('employedCookService',['$http', function($http){

	this.checkRights = function(){
		return $http.get("/cook/checkRights");
	}
	
	this.findCook = function(){
		return $http.get("/cook");
	}
	
	this.employedCooks = function(){
		return $http.get("/cook/employedCooks");
	}
	
	this.findAllOrders = function(){
		return $http.get("/cook/orders");
	}
	
	this.received = function(id, changeVersion){
		return $http.get("/cook/foodReceived/"+id + "/" + changeVersion);
	}
	
	this.receivedFood = function(){
		return $http.get("/cook/receivedFood");
	}
	
	this.ready = function(id){
		return $http.get("/cook/foodReady/"+id);
	}
	
	this.readyFood = function(id){
		return $http.get("/cook/readyFood");
	}
	
	this.changeProfile = function(id, cook){
		return $http.put("/cook/profile/"+id,cook);
	}
	
	this.changePassword = function(id, cook){
		return $http.put("/cook/changePassword/"+id,cook);
	}
	
	this.changedShiftDate = function(id){
		return $http.get("/cook/changedShiftDate/"+id);
	}
	
}]);