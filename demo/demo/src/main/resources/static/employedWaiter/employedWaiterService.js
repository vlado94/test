var services = angular.module('employedWaiter.services',['ngResource']);

services.service('employedWaiterService',['$http', function($http){
	
	this.checkRights = function(){
		return $http.get("/waiter/checkRights");
	}
	
	this.findWaiter = function(){
		return $http.get("/waiter");
	}
	
	this.readyFood = function(id){
		return $http.get("/waiter/readyFood");
	}
	
	this.readyDrinks = function(id){
		return $http.get("/waiter/readyDrinks");
	}
	
	this.readyOrders = function(id){
		return $http.get("/waiter/readyOrders");
	}
}]);