var services = angular.module('systemManager.services', ['ngResource']);

var baseUrl = 'http://localhost\\:8080';

services.service('systemManagerService', ['$http', function($http){
	
	this.checkRights = function(){
		return $http.get("/systemManager/checkRights");
	}
	
	this.findSystemManager = function(id){
		return $http.get("/systemManager");
	}
	
	this.findAllRestaurantManagers = function(){
		return $http.get("/systemManager/restaurantManager");
	}
	
	this.findAllFreeRestaurantManagers = function(){
		return $http.get("/systemManager/freeRestaurantManager");
	}
	
	this.saveRestaurantManager = function(restaurantManager){
		return $http.post("/systemManager/restaurantManager",restaurantManager);
	}
	
	this.updateSystemMangerProfile = function(systemManager){
		return $http.put("/systemManager/"+systemManager.id,systemManager);
	}
	
	this.findAllRestaurant = function(){
		return $http.get("/systemManager/restaurant");
	}
	
	this.saveRestaurant = function(restaurant){
		return $http.post("/systemManager/restaurant",restaurant);
	}
}]);
