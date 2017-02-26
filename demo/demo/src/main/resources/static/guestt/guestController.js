var app = angular.module('guest.controllers', []);
 
app.controller('guestController', ['$scope','$window','guestService', '$location',
  	function ($scope,$window, guestService, $location) {
		function checkRights() {			
			guestService.checkRights().then(
				function (response) {
					if(response.data === 'true'){
						//myValue = false;
						//document.getElementById("restaurantMenu").hide;
						findAll();

					}
					else {
						$location.path('login');
					}
				}
			);
		}
		checkRights();	
		
	
		function findAll(){
			guestService.restaurants().then(
				function(response){
					$scope.restaurants = response.data;
					$scope.orderByField = 'restaurants.name';
					
				}
			);
			
			guestService.orders().then(
				function(response){
					$scope.order = response.data;
				}
			)
			
			guestService.reservations().then(
				function(response){
					$scope.reservations = response.data;
				}
			)
			
			guestService.visitedRestaurants().then(
				function(response){
					$scope.visitedRestaurants = response.data;
				}
			)
		}
	
		
		$scope.getLoggedUser = function() {
			guestService.getLoggedUser().then(
				function (response) {
					$scope.loggedUser = response.data;
	            }		
			)
		}
		
		$scope.findFriends = function(){
			if($scope.inputStr !== '') {
				guestService.findFriends($scope.inputStr).then(
					function(response){
						$scope.guests = response.data;
					}
				)
			}
		}
		
		
		$scope.findRestaurants = function(){
			if($scope.findRestaurantStr !== '') {
				guestService.findRestaurants($scope.findRestaurantStr).then(
					function(response){
						$scope.foundRestaurants = response.data;
					//	alert("success");
					}
				)
			}
		}
		
		
		$scope.update = function() {
			guestService.updateGuestProfile($scope.loggedUser).then(
				function (response) {
                    alert("Successfully change.");
                    $scope.state = undefined;
                    $location.path('loggedIn/guest/profile');
                },
                function (response) {
                    alert("Error in changing.");
                }
			);
		}
		
		$scope.sendRequest = function(id) {
			guestService.sendRequest(id);
			$window.location.reload();
		}
		
		$scope.listFriends = function(){
			guestService.listFriends().then(
				function (response) {
					$scope.friends = [];	//pravim niz trojki: Guest za prikaz, status(pending, friends, rejected), id prijateljstva
					angular.forEach(response.data, function(value, key){
						if(value.friendSendRequest.id == $scope.loggedUser.id){	
							var guestAndStatus = {guest: value.friendReciveRequest, status: value.status, id:value.id };
							$scope.friends.push(guestAndStatus);
						}
						else{
							if(value.status == "Friends"){	//jer ne zelim da stoji pending ili rejected status kod primaoca zahteva
								var guestAndStatus = {guest: value.friendSendRequest, status: value.status,  id:value.id };
								$scope.friends.push(guestAndStatus);
							}
						}
					});
		        }		
			);
		}
		
		$scope.getRequests = function(){
			guestService.findAllRecivedPendingRequests().then(
					function (response) {
						$scope.recivedPending = response.data;
					}
			)
		}
		
		$scope.acceptRequest = function(id){
			guestService.acceptFriendRequest(id).then(
					function() {
						alert("Friend request acccepted.");
						$window.location.reload();
					}
			)
		}
		
		$scope.rejectRequest = function(id){
			guestService.rejectFriendRequest(id).then(
					function() {
						alert("Friend request rejected.");
						$window.location.reload();
					}
			)
		}
		
		$scope.unfriend = function(id){
			guestService.unfriend(id).then(
			function(){
				alert("Unfriend Successful.");
				$window.location.reload();
			})
		}
		
		$scope.menu = function(restaurant){
			guestService.find(restaurant.id).then(
					function(response){
						$scope.restaurantt = response.data;
						//document.getElementById("restaurantMenu").show;
						myMap(restaurant);
					},
					function(response){
						alert("Error while signal");
					}
			)
		}
		
		$scope.find = function(restaurant){
			guestService.find(restaurant.id).then(
				function(response){
					myMap(restaurant);
					if(restaurant.id != $scope.restaurantt.id){
						$scope.restaurantt = [];
					}
					
				},
				function(response){
					alert("Error while signal");
				}
			);
		}
		

		$scope.orderFood = function(dish){
			guestService.orderFood(dish.id, $scope.order).then(
				function(response){
					$scope.order = response.data;
					$scope.state = undefined;
										
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		
		$scope.removeDish = function(dish){
			guestService.removeFood(dish.id, $scope.order).then(
				function(response){
					$scope.order = response.data;
					$scope.state = undefined;
				}
			)
		}
		
		$scope.removeDrink = function(drink){
			guestService.removeDrink(drink.id, $scope.order).then(
				function(response){
					
					$scope.order = response.data;
					$scope.state = undefined;
					
				}
			)
		}
		
		$scope.orderDrink = function(drink){
			guestService.orderDrink(drink.id, $scope.order).then(
				function(response){
					$scope.state = undefined;
					$scope.order = response.data;
					
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		
		$scope.nextToOrders = function(restaurant){
			guestService.nextToOrders(restaurant.id).then(
				function(response){
					$scope.state = undefined;
					$scope.restaurantOrders = response.data;
					$location.path('loggedIn/guest/restaurantOrders');
					findAll();
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		
		$scope.makeOrder = function(order){
			guestService.makeOrder($scope.chosenTable, $scope.reservations.length, $scope.order).then(
				function(response){
					
					$scope.state = undefined;
					$scope.order = null;
					findAll();
					$location.path('loggedIn/guest/home');
				}
			);
		}
		
		$scope.reservation1 = function(id){
			guestService.find(id).then(
				function(response){
					$scope.restaurant = response.data;
					$location.path('loggedIn/guest/reservation1');
			});
		}
		
		$scope.reservation2 = function(){
			$location.path('loggedIn/guest/reservation2');
		}
		
		$scope.reservation3 = function(){
			$location.path('loggedIn/guest/reservation3');
		}
		
		$scope.rateOrder = function(orderRate, order){
			guestService.rateOrder(orderRate, order.id).then(
				function(response){
					if(response.data == ""){
						alert("Vec ste ocenili narudzbinu");
					}else{
						$scope.state = undefined;
						findAll();
					}
				},
				function(response){
					alert("Error in signal");
				});
		}
		
		$scope.rateService = function(serviceRate, order){
			guestService.rateService(serviceRate, order.id).then(
				function(response){
					if(response.data == ""){
						alert("Vec ste ocenili uslugu");
					}else{
						$scope.state = undefined;
						findAll();
					}
				},
				function(response){
					alert("Error in signal");
				});
		}
		
		$scope.rateRestaurant = function(restaurantRate, restaurant){
			guestService.rateRestaurant(restaurantRate, restaurant.id).then(
				function(response){
					if(response.data == ""){
						alert("Vec ste ocenili restoran");
					}else{
						$scope.state = undefined;
						findAll();
					}
				},
				function(response){
					alert("Error in signal");
				});
		}
		
		/*$scope.reservation4 = function(){
			$location.path('loggedIn/guest/reservation4');
		}*/
		$scope.loadTables = function(id){
			guestService.getTables(id).then(
					function(response){
						var stolovi = [];
						var red = [];
						var lastXPos = 0;
						var counter = 0;
						angular.forEach(response.data, function(value, key){	// punjenje matrice stolova
							counter++;
							
							angular.forEach(value.reservations, function(res,key){// for za proveru rezervisanog stola
								if(res.date===$scope.reservation.date){
									if(((res.hours + res.minutes/60+res.duration) >= ($scope.reservation.hours+$scope.reservation.minutes/60)) &&
										(($scope.reservation.hours+$scope.reservation.minutes/60+$scope.reservation.duration)>=(res.hours + res.minutes/60) )	){
									value.status = "Reserved";
									}
								}
							});
							
							if(value.xpos == lastXPos){	
								red.push(value);
							}
							if((value.xpos != lastXPos) || counter==response.data.length ) {
								stolovi.push(red);
								red =[];
								red.push(value);
							}
							lastXPos = value.xpos;
						});
						$scope.tables = stolovi;
					});
		}
		
		$scope.openModal = function(){
			var modal = document.getElementById('myModal');
			modal.style.display = "block";
		}
		
		$scope.closeModal = function(){
			var modal = document.getElementById('myModal');
			modal.style.display = "none";
			//$window.location.reload();
			
		}
		
		
		
		$scope.selectTable = function(table){
			$("#odabranSto").html("Chosen table: "+table.name);
			$scope.chosenTable = table.id;
		}
		
		
		$scope.makeReservation = function(){
			guestService.makeReservation($scope.chosenTable, $scope.reservation).then(
					function(response){
						 
						$location.path('loggedIn/guest/reservation4');
					});
		}
		
		$scope.orderForReservation = function(){
			guestService.makeReservation($scope.chosenTable, $scope.reservation).then(
					function(response){
						$location.path('loggedIn/guest/reservation4');
					});
		}
		
		$scope.dontOrderForReservation = function(){
			guestService.makeReservation($scope.chosenTable, $scope.reservation).then(
					function(response){
						$location.path('loggedIn/guest/home');
					});
		}
		
				
		function myMap(restaurant) {
			var mapProp= {
			    zoom:15,
			    mapTypeId: google.maps.MapTypeId.ROADMAP
			};
			
			var map=new google.maps.Map(document.getElementById("googleMap2"),mapProp);
			pos = [];
						
			geocoder = new google.maps.Geocoder();
			address = restaurant.street + " " + restaurant.number + " , " + restaurant.city + " , " + restaurant.country; 
			geocoder.geocode( { 'address': address}, function(results, status) {
			      if (status == 'OK') {
			        map.setCenter(results[0].geometry.location);
			        var marker = new google.maps.Marker({
			            map: map,
			            position: results[0].geometry.location
			        });
			        
			        var flightPath = new google.maps.Polyline({
					    path: [results[0].geometry.location, pos],
					    strokeColor: "#0000FF",
					    strokeOpacity: 0.8,
					    strokeWeight: 2
					  });
			        flightPath.setMap(map);
			      } else {
			        alert('Geocode was not successful for the following reason: ' + status);
			      }
			    });
		}	
		
		$scope.customOrder = function(dish) {
			   return dish.typeOfDish === 'Salad' ? 3
			          : dish.typeOfDish === 'Cooked' ? 2 
			          : 1
			};
			
			
		
}]);
