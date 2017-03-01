var app = angular.module('bidder.controllers', []);
 
app.controller('bidderController', ['$scope','bidderService', '$location',
  	function ($scope, bidderService, $location) {
	
		//provera da li je logovan ponudjac
		function checkRights() {
			bidderService.checkRights().then(
				function (response) {
					if(response.status == 200) {
						$scope.bidder = response.data;
						getOffers();
						getActiveOffers();
					}
					else {
					    $location.path('login');
					    alert("Access denied!");
				    }
				}
			);
		}
		checkRights();

		// izlistavanje svih ponuda na koje je do sada konkurisao logovani ponudjac
		function getOffers() {
			bidderService.getOffers().then(
				function (response) {
					data = response.data;
					result = []
					correctOrder = null
					//izlistavanje ponuda samo za tog ponudjaca od svih mogucih, iz restorana gde je taj ponudjac dao ponudu
					for(i = 0;i<data.length;i++) {
						for(j = 0;j<data[i].offers.length;j++) {
							if(data[i].offers[j].bidder.id === $scope.bidder.id) {
								correctOffer = data[i].offers[j]
								correctOrder = data[i]
								correctOrder.offers = correctOffer
								result.push(correctOrder)
							}
						}
					}
					
					//konverzija vremena za lep prikaz, jer daje u ms
					$scope.restaurantOrders = result
				}
	        );
		}
		

		// izlistavanje svih ponuda za logovanog ponudjaca od svih restorana gde
		// moze da konkurise
		function getActiveOffers() {
			bidderService.getActiveOffers().then(
				function (response) {
					result = [];
					for(var i=0;i<response.data.length;i++) {
						var res = checkIfInLastOrders(response.data[i])
						if(res != "")
							result.push(response.data[i]);
					}
					$scope.restaurantOrdersFromAllRestaurants = result;
				}
	        );
		}
		
		function checkIfInLastOrders(object) {
			var ord1 = [];
			if(object.dish == null)
				ord1 = object.drink.name
			else
				ord1 = object.dish.name;
			for(var i=0;i<$scope.restaurantOrders.length;i++) {
				var ord2 = [];
				if($scope.restaurantOrders[i].dish == null)
					ord2 = $scope.restaurantOrders[i].drink.name
				else
					ord2 = $scope.restaurantOrders[i].dish.name;
				if(ord1 == ord2)
					return "";				
			}
			return "ok"
		}
		
		//kliknuto je na neku startu ponudu ponudjaca za promenu, prikazati je u formi
		$scope.change = function(restaurantOrder){
			x = document.getElementById("artical");
			if(restaurantOrder.dish != null)
				x.setAttribute("value", restaurantOrder.dish.name);
			else if(restaurantOrder.drink != null)
				x.setAttribute("value", restaurantOrder.drink.name);
			y = document.getElementById("price");
			price = restaurantOrder.offers.price;
			y.setAttribute("value", price);
			$scope.restaurantOrderForChange = restaurantOrder;
		}
		
		//kliknuto je na neku ponudu restorana za takmicenje, prikazati je u formi
		$scope.compete = function(restaurantOrder){
			x = document.getElementById("articalForCompete");
			if(restaurantOrder.dish != null)
				x.setAttribute("value", restaurantOrder.dish.name);
			else if(restaurantOrder.drink != null)
				x.setAttribute("value", restaurantOrder.drink.name);
			$scope.restaurantOrderForCompete = restaurantOrder;
		}
		
		//probati da se promeni vrednost ponude koja je prethodno odabrana
		$scope.changeOffer = function(){
			offers = $scope.restaurantOrderForChange.offers;
			offer1 = $scope.offer1;
			offer1.id = offers.id;
			offer1.bidder = offers.bidder;
			offer1.accepted = offers.accepted;
			bidderService.changeOffer($scope.restaurantOrderForChange,offer1).then(
				function (response) {
					if(response.data == 'ok') {
	                	location.reload(true);
					}
					else
						alert('Error.Maybe is offer closed.');
				}
		    );
		}
		
		//dodavanje nove ponude u neki restoran
		$scope.competeWithInsertedValue = function(){
			bidderService.competeWithInsertedValue($scope.restaurantOrderForCompete,$scope.offer).then(
				function (response) {
					if(response.data == 'ok') {
	                	location.reload(true);
					}
					else
						alert('Forbidden operation.Maybe you have one offer for that order or maybe oreder is closed.');
				}
		    );
		}
		
		$scope.update = function() {
			bidderService.updateBidderProfile($scope.bidder).then(
				function (response) {
                    alert("Successfully change.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('login');
                },
                function (response) {
                    alert("Error in changing.");
                }
			);
		}
	}
]);