var app = angular.module('restaurantManager.controllers', []);

app.controller('restaurantManagerController', ['$scope','$window','restaurantManagerService', '$location',
	function ($scope,$window, restaurantManagerService, $location) {
		function checkRights() {
			restaurantManagerService.checkRights().then(
				function (response) {
					if(response.status == 200) {
						$scope.restaurantManager = response.data;
						findAll();
						showFreeBidders();
					}
					else {
					    $location.path('login');
					    alert("Access denied!");
				    }
				}
			);
		}
		checkRights();
		
		function findAll() {
			restaurantManagerService.findRestaurant().then(
				function (response) {
					$scope.restaurant = response.data;
					$scope.restaurantOrders = response.data.restaurantOrders;
					for(w = 0;w<$scope.restaurantOrders.length;w++) {
						$scope.restaurantOrders[w].startDate = new Date($scope.restaurantOrders[w].startDate).toDateString();
						$scope.restaurantOrders[w].endDate = new Date($scope.restaurantOrders[w].endDate).toDateString();
					}
					$scope.waiters = response.data.waiters;
					$scope.cooks = response.data.cooks;
					$scope.bartenders = response.data.bartenders;
					$scope.bidders = response.data.bidders;

					$scope.averageRate = calculateAverageRate(response.data.rateRestaurant);
					myMap();
					
				}
	        );
		}
		
		function calculateAverageRate (rates) {
			var rate = 0;
			if(rates.length > 0)
				for (var i =0;i<rates.length;i++) {
					rate += rates[i].rate;
				}
			else
				return "/";
			return rate/rates.length;
		}
		
		function showFreeBidders() {
			restaurantManagerService.showFreeBidders().then(
				function (response) {
					$scope.freeBidders = response.data;
				}
	        );
		}
		
		$scope.saveDrink = function() {
			restaurantManagerService.saveDrink($scope.drink).then(
				function (response) {
                    alert("Successfully added.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/restaurantManager/info');
                },
                function (response) {
                    alert("Error in adding.");
                }
			);
		}
		
		$scope.saveDish = function() {
			restaurantManagerService.saveDish($scope.dish).then(
				function (response) {
                    alert("Successfully added.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/restaurantManager/info');
                },
                function (response) {
                    alert("Error in adding.");
                }
			);
		}
		
		$scope.demissionBartender = function(bartender) {
			if(bartender.orders.length < 0) {
				restaurantManagerService.deleteBartender(bartender).then(
				function (response) {
                    alert("Successfully removed.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/restaurantManager/info');
                },
                function (response) {
                    alert("Error in deleting.");
                }
				);
			}
			else
		        alert("Forrbidden becouse he have orders.");
		}
		
		$scope.demissionWaiter = function(waiter) {
			if(waiter.orders.length < 0) {
				restaurantManagerService.deleteWaiter(waiter).then(
				function (response) {
                    alert("Successfully removed.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/restaurantManager/info');
                },
                function (response) {
                    alert("Error in deleting.");
                }
				);
			}
			else
		        alert("Forrbidden becouse he have orders.");
		}
		
		$scope.demissionCook = function(cook) {
			if(cook.orders.length < 0) {
				restaurantManagerService.deleteCook(cook).then(
					function (response) {
	                    alert("Successfully removed.");
	                    $scope.state = undefined;
	                    findAll();
	                    $location.path('loggedIn/restaurantManager/info');
	                },
	                function (response) {
	                    alert("Error in deleting.");
	                }
				);
			}
			else
		        alert("Forrbidden becouse he have orders.");
            
		}
		
		$scope.update = function() {
			restaurantManagerService.updateMangerProfile($scope.restaurantManager).then(
				function (response) {
                    alert("Successfully change.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/restaurantManager/info');
                },
                function (response) {
                    alert("Error in changing.");
                }
			);
		}
		
		$scope.saveBidder = function() {
			restaurantManagerService.saveBidder($scope.bidder).then(
				function (response) {
                    alert("Successfully added.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/restaurantManager/info');
                },
                function (response) {
                    alert("Error in adding.");
                }
			);
		}
		
		$scope.connectBidder = function(bidder) {
			restaurantManagerService.connectBidder(bidder).then(
				function (response) {
                    alert("Successfully added.");
                    $scope.state = undefined;
                    findAll();
                    $location.path('loggedIn/restaurantManager/info');
                },
                function (response) {
                    alert("Error in adding.");
                }
			);
		}
		
		
		
		
		$scope.saveEmployed = function() {
			//$scope.drink.restaurant = $scope.restaurant;
			if($scope.employedType == 'Waiter') {
				$scope.employed.tablesForHandling = tablesForNewWaiter;
				restaurantManagerService.saveWaiter($scope.employed).then(
					function (response) {
	                    alert("Successfully added.");
	                    $scope.state = undefined;
	                    findAll();
	                    $location.path('loggedIn/restaurantManager/info');
	                },
	                function (response) {
	                    alert("Error in adding.");
	                }
				);
			}
			else if($scope.employedType == 'Cook') {
				cookType = document.getElementById("cookType").value;

				restaurantManagerService.saveCook($scope.employed,cookType).then(
					function (response) {
						alert("Successfully added.");
		                $scope.state = undefined;
		                findAll();
		                $location.path('loggedIn/restaurantManager/info');
		            },
		            function (response) {
		            	alert("Error in adding.");
		            }
				);
			}
			else if($scope.employedType == 'Bartender') {
				restaurantManagerService.saveBartender($scope.employed).then(
					function (response) {
						alert("Successfully added.");
		                $scope.state = undefined;
		                findAll();
		                $location.path('loggedIn/restaurantManager/info');
					},
		            function (response) {
						alert("Error in adding.");
		            }
				);
			}
		}	
		
		$scope.offerDetails = function(restaurantOrder) {
			$scope.restaurantOrderDetails = restaurantOrder;
            $location.path('loggedIn/restaurantManager/offerDetails');
		}
		
		$scope.acceptRestaurantOrder = function(offer) {
			restaurantOrderr = $scope.restaurantOrderDetails;
			if(restaurantOrderr.orderActive == "open") {
				restaurantOrderr.idFromChoosenBidder = offer.bidder.id;
				restaurantOrderr.startDate = new Date(restaurantOrderr.startDate).getTime();
				restaurantOrderr.endDate = new Date(restaurantOrderr.endDate).getTime();
				restaurantManagerService.acceptRestaurantOrder(restaurantOrderr).then(
					function (response) {
	                    alert("Successfully added.");
	                    $scope.state = undefined;
	                    findAll();
	                    $location.path('loggedIn/restaurantManager/info');
	                },
	                function (response) {
	                    alert("Error in adding.");
	                }
				);
			}else
				alert('Offer is closed.')
			
		}
		
		$scope.makeConfig = function(){
			var xaxis = $("input[name='xaxis']").val();
			var yaxis = $("input[name='yaxis']").val();
			restaurantManagerService.makeConfig(xaxis, yaxis).then(
				function(response){
					$window.location.reload();
				});
		}
		
		$scope.loadTables = function(){
			restaurantManagerService.getTables().then(
					function(response){
						//tables = response.data;
						
						var stolovi = [];
						var red = [];
						var lastXPos = 0;
						var counter = 0;
						angular.forEach(response.data, function(value, key){	// punjenje matrice stolova
							counter++;
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
		
		$scope.addSegment = function(){
			restaurantManagerService.addSegment($scope.segment).then(
				function(response){
					$("input[name='segName']").val("");
				});
		}
		
		
		$scope.openModal = function(id){
			restaurantManagerService.getSegments().then(
				function(response){
					$scope.segments = response.data;
					$scope.tableId = id;
					var modal = document.getElementById('myModal');
					modal.style.display = "block";
				});
		}
		
		
		
		$scope.closeModal = function(){
			restaurantManagerService.updateTable($scope.tableId, $scope.table).then(
				function(response){
					var modal = document.getElementById('myModal');
					modal.style.display = "none";
					$window.location.reload();
				});
		}
		
		$scope.createNewOffer = function() {
			drink = $scope.newRestaurantOrder.drink
			dish = $scope.newRestaurantOrder.dish
			$scope.newRestaurantOrder.startDate = new Date($scope.newRestaurantOrder.startDate).toISOString();
			$scope.newRestaurantOrder.endDate = new Date($scope.newRestaurantOrder.endDate).toISOString();
			if (dish.id === "")
				$scope.newRestaurantOrder.dish = null;
			if (drink.id === "")
				$scope.newRestaurantOrder.drink = null;
				
			if(((dish === undefined || dish.id === "") && drink.id !== "") || (dish.id !== "" && (drink == null || drink.id === ""))) {
				restaurantManagerService.createNewOffer($scope.newRestaurantOrder).then(
					function (response) {
	                    alert("Successfully added.");
	                    $scope.state = undefined;
	                    findAll();
	                    $location.path('loggedIn/restaurantManager/info');
	                },
	                function (response) {
	                    alert("Error in adding.");
	                }
				);
			}
			else
				alert("Only one select field can be choose.");
		}
		
		function myMap() {
			var mapProp= {
			    zoom:15,
			    mapTypeId: google.maps.MapTypeId.ROADMAP
			};
			pos = [];

			map=new google.maps.Map(document.getElementById("googleMap2"),mapProp);
			if (navigator.geolocation) {
			    navigator.geolocation.getCurrentPosition(function(position) {
			    	pos = {
			            lat: position.coords.latitude,
			            lng: position.coords.longitude
			        };
			        var marker = new google.maps.Marker({
			        	position:pos,
			        });

			        marker.setMap(map);
			        map.setCenter(pos);
			    }, function() {
			        handleLocationError(true, infoWindow, map.getCenter());
			    });
			}
			
			geocoder = new google.maps.Geocoder();
			address = $scope.restaurant.street + " " + $scope.restaurant.number + " , " + $scope.restaurant.city + " , " + $scope.restaurant.country; 
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
		
		$scope.changeShiftCookAction = function(){
			restaurantManagerService.changeShiftCookAction($scope.changeShiftCook).then(
				function(response){
					alert("Successfully added.");
                   });
		}
		
		$scope.changeShiftBartenderAction = function(){
			restaurantManagerService.changeShiftBartenderAction($scope.changeShiftBartender).then(
				function(response){
					alert("Successfully added.");
                   });
		}
		
		$scope.changeShiftWaiterAction = function(){
			restaurantManagerService.changeShiftWaiterAction($scope.changeShiftWaiter).then(
				function(response){
					alert("Successfully added.");
                   });
		}
		
		$scope.getWaiterWithInputName = function(){
			restaurantManagerService.getWaiterWithInputName($scope.waiterName).then(
				function(response){
					if(response.data != "") {
						alert("Successfully find waiter.");
						$scope.waiterRate = response.data.rate;
					} else
						alert("Waiter doesn't exist.");
		   });
		}
		
		$scope.geDishWithInputName = function(){
			restaurantManagerService.geDishWithInputName($scope.dishName).then(
					function(response){
						if(response.data != "-1.0") {
							alert("Successfully find dish.");
							$scope.dishRate = response.data;
						} else
							alert("Dish doesn't exist.");
			   });
		}
		
		$scope.calculateRevenues = function(){
			$scope.restaurantRevenues = 0;
			for(var i=0;i<$scope.restaurant.waiters.length;i++) {
				for(var j=0;j<$scope.restaurant.waiters[i].bills.length;j++) {
					if(new Date($scope.fromDateRevenues).getTime() < new Date($scope.restaurant.waiters[i].bills[j].date).getTime() < new Date($scope.toDateRevenues).getTime())
						$scope.restaurantRevenues += $scope.restaurant.waiters[i].bills[j].total;
				}
			}
		}
		
		$scope.showRevenuesForWaiters = function(){
			$scope.revenuesWaiter = [];
			for(var i=0;i<$scope.restaurant.waiters.length;i++) {
				revenues = 0;
				for(var j=0;j<$scope.restaurant.waiters[i].bills.length;j++) {
					revenues += $scope.restaurant.waiters[i].bills[j].total;
				}
				var waiter = {name:"", revenues:"0"};
				waiter['name'] = $scope.restaurant.waiters[i].firstname;
				waiter['revenues'] = revenues;
				$scope.revenuesWaiter.push(waiter);
			}
		}
		
		$scope.showNumOfVisitorsForWeek = function() {
			dataVisitors = getDataForWeek();
			var ctx = document.getElementById('myChartWeek').getContext('2d');
			var myChart = new Chart(ctx, {
			  type: 'bar',
			  data: {
			    labels: ['M', 'T', 'W', 'T', 'F', 'S', 'S'],
			    datasets: [{
			      label: 'Visitors',
			      data: dataVisitors,
			      backgroundColor: "rgba(153,255,51,1.0)"
			    }]
			  }
			});			
		}
		
		$scope.showNumOfVisitorsForDay = function() {
			dataVisitors = getDataForDay();
			var ctx = document.getElementById('myChartDay').getContext('2d');
			var myChart = new Chart(ctx, {
			  type: 'bar',
			  data: {
			    labels: ['9', '11', '13', '15', '17', '19', '21'],
			    datasets: [{
			      label: 'Visitors',
			      data: dataVisitors,
			      backgroundColor: "rgba(153,255,51,1.0)"
			    }]
			  }
			});			
		}
		
		function getDataForDay() {
			//uzimanje svih racuna iz restorana
			bills = [];
			visitors = [0,0,0,0,0,0,0];
			for(var i =0;i<$scope.restaurant.waiters.length;i++) {
				for(var j =0;j<$scope.restaurant.waiters[i].bills.length;j++) {
					bills.push($scope.restaurant.waiters[i].bills[j]);
				}
			}
			var requestedDate = new Date($scope.chartForDay);
			reservationsThatDay = [];
			for(var j =0;j<bills.length;j++) {
				var day = new Date(bills[j].date);
				if(requestedDate.getFullYear() == day.getFullYear() && requestedDate.getMonth() == day.getMonth() && requestedDate.getDate() == day.getDate())
					reservationsThatDay.push(bills[j]);
			}
			
			for(var j =0;j<reservationsThatDay.length;j++) {
				var hours = reservationsThatDay[j].reservation.hours;
				var termin = findClosestTermin(hours);
				visitors[termin] += reservationsThatDay[j].reservation.guests.length;	
			}
			return visitors;		
		}
		
		function getDataForWeek() {
			//uzimanje svih racuna iz restorana
			bills = [];
			visitors = [0,0,0,0,0,0,0];
			for(var i =0;i<$scope.restaurant.waiters.length;i++) {
				for(var j =0;j<$scope.restaurant.waiters[i].bills.length;j++) {
					bills.push($scope.restaurant.waiters[i].bills[j]);
				}
			}
			var requestedDate = new Date($scope.chartForWeek);
			var requestedWeekForDate = getWeekNumber(requestedDate);
			reservationsThatWeek = [];
			for(var j =0;j<bills.length;j++) {
				var day = new Date(bills[j].date);
				var weekForDate = getWeekNumber(day);
				if(weekForDate[0] == requestedWeekForDate[0] && weekForDate[1] == requestedWeekForDate[1])
					reservationsThatWeek.push(bills[j]);
			}
			
			for(var j =0;j<reservationsThatWeek.length;j++) {
				var day = new Date(reservationsThatWeek[j].date).getDay();
				visitors[day-1] += reservationsThatWeek[j].reservation.guests.length;	
			}
			return visitors;		
		}
		
		function findClosestTermin(houerInDay) {
			if(houerInDay <= 10)
				return 0;
			else if(houerInDay <= 12)
				return 1;
			else if(houerInDay <= 14)
				return 2;
			else if(houerInDay <= 16)
				return 3;
			else if(houerInDay <= 18)
				return 4;
			else if(houerInDay <= 20)
				return 5;
			else if(houerInDay <= 22)
				return 6;			
		}	
		
		function getWeekNumber(d) {
		    // Copy date so don't modify original
		    d = new Date(+d);
		    d.setHours(0,0,0,0);
		    // Set to nearest Thursday: current date + 4 - current day number
		    // Make Sunday's day number 7
		    d.setDate(d.getDate() + 4 - (d.getDay()||7));
		    // Get first day of year
		    var yearStart = new Date(d.getFullYear(),0,1);
		    // Calculate full weeks to nearest Thursday
		    var weekNo = Math.ceil(( ( (d - yearStart) / 86400000) + 1)/7);
		    // Return array of year and week number
		    return [d.getFullYear(), weekNo];
		}
		
		$scope.updateRestaurant = function(){
			restaurantManagerService.updateRestaurant($scope.restaurant).then(
				function(response){
					alert("Successfully changed.");
                 });
		}
		
		$scope.changeDish = function(dish){
			x = document.getElementById("artical");
			x.setAttribute("value", dish.name);
			y = document.getElementById("count");
			y.setAttribute("value", dish.count);
			$scope.dishForChange = dish;
		}
		
		$scope.changeDrink = function(drink){
			x = document.getElementById("articalDrink");
			x.setAttribute("value", drink.name);
			y = document.getElementById("countDrink");
			y.setAttribute("value", drink.count);
			$scope.drinkForChange = drink;
		}
		
		$scope.tryToChangeDish = function(){
			dishName = document.getElementById("artical").value;
			dishCount = document.getElementById("count").value;
			restaurantManagerService.tryToChangeDish($scope.dishForChange,dishName,dishCount).then(
				function(response){
					$window.location.reload();
					alert("Successfully changed.");
	            });
		}
		
		$scope.tryToChangeDrink = function(){
			drinkName = document.getElementById("articalDrink").value;
			drinkCount = document.getElementById("countDrink").value;
			restaurantManagerService.tryToChangeDrink($scope.drinkForChange,drinkName,drinkCount).then(
				function(response){
					alert("Successfully changed.");
					$window.location.reload();
	            });
		}
		
		tablesForNewWaiter = [];
		$scope.loadTables = function(){
			restaurantManagerService.getTables().then(
					function(response){
						var stolovi = [];
						var red = [];
						var lastXPos = 0;
						var counter = 0;
						angular.forEach(response.data, function(value, key){	// punjenje matrice stolova
							counter++;
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
		
		$scope.addTableInListFowWaiter = function(id){
			tablesForNewWaiter.push(id);
		}

		$scope.changedShift = function() {
		    var today = Date.now();
		    var tomorrow = new Date(Date.now() + 86400000 * 1);
			var step = 2;
			var datesArr = [];
			var temp = 0;
			if($scope.employed.defaultShift != "First") 
				temp = 1;
			else
				temp = 0;
			for(var i = 0;i<300;i++) {
				day = new Date(Date.now() +temp * 86400000 + 86400000 *  i*step);
				datesArr.push(day);
			}
			$('#date').multiDatesPicker('destroy');
			$('#date').multiDatesPicker({
		        numberOfMonths: 1,
		        addDates: datesArr
			});
			if(temp == 1)
				$('#date').multiDatesPicker('toggleDate', new Date());
		}
}]);