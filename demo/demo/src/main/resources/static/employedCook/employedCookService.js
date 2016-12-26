var services = angular.module('employedCook.services',['ngResource']);

var baseUrl = 'http://localhost\\:8080';

services.service('employedCookService',['$http', function($http){

	this.checkRights = function(){
		return $http.get("/cook/checkRights");
	}
	
	this.findCook = function(){
		return $http.get("/cook");
	}
	
	this.findAllOrders = function(){
		return $http.get("/cook/orders");
	}
	
	this.received = function(id){
		return $http.get("/cook/foodReceived/"+id);
	}
	
	this.receivedFood = function(id){
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
}]);