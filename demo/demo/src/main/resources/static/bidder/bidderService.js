var services = angular.module('bidder.services', ['ngResource']);

var baseUrl = 'http://localhost\\:8080';

services.service('bidderService', ['$http', function($http){
	
	this.checkRights = function(){
		return $http.get("/bidder/checkRights");
	}
	
	this.findBidder = function(){
		return $http.get("/bidder");
	}	
}]);