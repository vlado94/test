var services = angular.module('bidder.services', ['ngResource']);

var baseUrl = 'http://localhost\\:8080';

services.service('bidderService', ['$http', function($http){
	
	this.checkRights = function(){
		return $http.get("/bidder/checkRights");
	}

	this.getOffers = function(){
		return $http.get("/bidder/getOffers");
	}
	
	this.changeOffer = function(restaurantOrderForChange,offer){
		return $http.post("/bidder/changeOffer/"+restaurantOrderForChange.id,offer);
	}
	
	this.getActiveOffers = function(){
		return $http.get("/bidder/getActiveOffers");
	}
	
	this.competeWithInsertedValue = function(restaurantOrderr,offer){
		return $http.post("/bidder/competeWithInsertedValue/"+restaurantOrderr.id,offer);
	}
	
	this.updateBidderProfile = function(bidder){
		return $http.get("/bidder/updateBidderProfile/"+bidder.firstname + "/"+bidder.lastname+ "/"+bidder.password);
	}
}]);