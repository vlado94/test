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
	
	this.findRestaurants = function(input){
		return $http.get("/guest/findRestaurant/"+input);
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
	
	this.restaurants = function(){
		return $http.get("/guest/restaurants");
	}
	
	this.find = function(id){
		return $http.get("/guest/restaurant/"+id);
	}
	

	this.orders = function(){
		return $http.get("/guest/order");
	}
	
	this.orderFood = function(id, order){
		return $http.put("/guest/addDish/"+id, order);
	}
	
	this.removeFood = function(id, order){
		return $http.put("/guest/removeDish/"+id, order);
	}
	
	this.removeDrink = function(id, order){
		return $http.put("/guest/removeDrink/"+id, order);
	}
	
	this.orderDrink = function(id, order){
		return $http.put("/guest/addDrink/"+id, order);
	}
	
	this.makeOrder = function(tableId, reservationId, order){
		return $http.post("/guest/makeOrder/"+tableId.id +"/"+ reservationId, order);
	}
	

	this.getTables = function(id){
		return $http.get("guest/restaurant/getTables/"+id);
	}
	
	this.makeReservation = function(id, reservation){
		return $http.post("guest/makeReservation", reservation);
	}
	
	this.reservations = function(){
		return $http.get("guest/reservations");
	}
	
	this.visitedRestaurants = function(){
		return $http.get("guest/visitedRestaurants");
	}
	
	this.nextToOrders = function(id){
		return $http.get("guest/restaurantOrders/"+id);
	}
	
	this.rateRestaurant = function(restaurantRate, restaurant){
		return $http.put("/guest/rateRestaurant/"+restaurantRate +"/"+ restaurant);
	}
	
	this.rateOrder = function(orderRate, order){
		return $http.put("/guest/rateOrder/"+orderRate +"/"+ order);	
	}
	
	this.rateService = function(serviceRate, order){
		return $http.put("/guest/rateService/"+serviceRate +"/"+ order);	
	}
	
	this.acceptInvite = function(id){
		return $http.post("/guest/acceptInvite/"+id);	
	}
	this.rejectInvite = function(id){
		return $http.post("/guest/rejectInvite/"+id);	
	}
	
	this.avgRateFriends = function(id){
		return $http.get("/guest/avgRateFriends/" + id);
	}
	
	this.getCurrentReservations = function(){
		return $http.get("/guest/currentReservations");
	}
	
	this.cancelReservation = function(id){
		return $http.get("/guest/cancelReservation/"+id);
	}
	
	this.checkTables = function(tables){
		return $http.post("/guest/checkTables",tables);
	}

	
}]);