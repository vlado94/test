var services = angular.module('employedBartender.services',['ngResource']);

var baseUrl = 'http://localhost\\:8080';

services.service('employedBartenderService',['$http', function($http){
		
	this.checkRights = function(){
		return $http.get("/bartender/checkRights");
	}
	
	this.findBartender = function(){
		return $http.get("/bartender");
	}
	
	this.findAllOrdres = function(){
		return $http.get("/bartender/orders");
	}
	
	this.ready = function(id){
		return $http.get("/bartender/drinkReady/"+id);
	}
	
	this.readyDrinks = function(id){
		return $http.get("/bartender/readyDrinks");
	}
	
	this.changeProfile = function(bartender){
		return $http.put("/bartender/profile",bartender);
	}
}]);