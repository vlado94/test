var app = angular.module('employedWaiter.controllers', []);

app.controller('employedWaiterController', [ '$scope', 'employedWaiterService','$location',
	function($scope, employedWaiterService, $location) {
	
	  	$scope.today = function() {
		    $scope.dt = new Date();
		  };
		$scope.today();
		function checkRights(){
			employedWaiterService.checkRights().then(
				function(response){
					if(response.data === 'true')
						findAll();
					else{
						$location.path('login')
						alert("Access denied");
					}
				}
			);
		}
		checkRights();
		
		function findAll(){
			employedWaiterService.findWaiter().then(
				function(response){
					$scope.waiter = response.data;
				}
			);
			
			employedWaiterService.employedWaiters().then(
					function(response){
						$scope.employedWaiters = response.data;
					}
				)
			
			employedWaiterService.orders().then(
				function(response){
					$scope.orders = response.data;
				}
			)
			
			employedWaiterService.restaurant().then(
				function(response){
					$scope.restaurant = response.data;
				}
			)
						
			employedWaiterService.readyOrders().then(
				function(response){
					$scope.readyOrders = response.data;
				}
			);
			
			employedWaiterService.reservations().then(
				function(response){
					$scope.reservations = response.data;
				}
			);
			
			employedWaiterService.newOrder().then(
				function(response){
					$scope.newOrder = response.data;
				}
			)
			
			employedWaiterService.tables().then(
				function(response){
					$scope.waiterTables = response.data;
				}
			)
			
			employedWaiterService.changeOrdersList().then(
				function(response){
					$scope.changeOrdersList = response.data;
				}
			)
			
			$scope.reservation = "";
		}
		
		$scope.changedValue = function(reservationId) {
			//findAll();
		    $scope.reservation = reservationId.id;
		    employedWaiterService.newOrder().then(
					function(response){
						$scope.newOrder = response.data;
					}
				)
		    
		  }  
		
		$scope.makeBill = function(order){
			employedWaiterService.makeBill(order).then(
				function(response){
					$scope.readyOrders.splice($scope.readyOrders.indexOf(order),1);
				},
				function(response){
					alert("Error while signal");
				}
			);
		}	
		
		$scope.showShift = function(){
			$scope.shift = datePicker();
		}
		
		$scope.sendToEmployed = function(order){
			employedWaiterService.sendToEmployed(order.id).then(
					function(response){
						$scope.orders.splice($scope.orders.indexOf(order),1);
						findAll();
					},
					function(response){
						alert("Error while signal");
					}
			);
		}
		
		$scope.changeOrder = function(order){
			employedWaiterService.changeOrder(order.id, order.version).then(
					function(response){
						if(response.data == ""){
							alert("You can't change this order anymore");
							findAll();
						} else {
							$scope.changedOrder = response.data;
							$location.path('loggedIn/waiter/changeOrder');
						}
			});				
		}
		
		$scope.orderFood = function(dish){
			employedWaiterService.orderFood(dish.id,$scope.changedOrder).then(
				function(response){
					$scope.changedOrder = response.data;
					$scope.state = undefined;
					findAll();
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		
		$scope.orderDrink = function(drink){
			employedWaiterService.orderDrink(drink.id, $scope.changedOrder).then(
				function(response){
					$scope.changedOrder = response.data;
					$scope.state = undefined;
					findAll();
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		
		$scope.removeDish = function(dish){
			employedWaiterService.removeFood(dish.id, $scope.changedOrder.id).then(
				function(response){
					$scope.changedOrder.food.splice($scope.changedOrder.food.indexOf(dish),1);
					
					$scope.state = undefined;
					findAll();
				}
			)
		}
		
		$scope.removeDrink = function(drink){
			employedWaiterService.removeDrink(drink.id, $scope.changedOrder.id).then(
				function(response){
					$scope.changedOrder.drinks.splice($scope.changedOrder.drinks.indexOf(drink),1);
					//$scope.changeOrder
					$scope.state = undefined;
					findAll();
				}
			)
		}
		
		$scope.makeOrder = function(){
			employedWaiterService.makeOrder($scope.changedOrder.id).then(
				function(response){
					$location.path('loggedIn/waiter/orders');
					findAll();
				}
			)
		}
		
		$scope.newOrderDrink = function(drink){
			employedWaiterService.newOrderDrink(drink.id, $scope.newOrder).then(
				function(response){
					$scope.newOrder = response.data;
					$scope.state = undefined;
					//findAll();
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		
		$scope.newOrderFood = function(dish){
			employedWaiterService.newOrderFood(dish.id, $scope.reservation, $scope.newOrder).then(
				function(response){
					$scope.newOrder = response.data;
					$scope.state = undefined;
					//findAll();
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		
		$scope.newOrderDrink = function(drink){
			employedWaiterService.newOrderDrink(drink.id, $scope.reservation, $scope.newOrder).then(
				function(response){
					
					$scope.newOrder = response.data;
					$scope.state = undefined;
					//findAll();
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		
		$scope.removeNewDish = function(dish){
			employedWaiterService.removeNewDish(dish.id, $scope.newOrder).then(
				function(response){
					$scope.newOrder = response.data;
					$scope.state = undefined;
					//findAll();
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		

		$scope.removeNewDrink = function(drink){
			employedWaiterService.removeNewDrink(drink.id, $scope.newOrder).then(
				function(response){
					$scope.newOrder = response.data;
					$scope.state = undefined;
					//findAll();
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		
		$scope.makeNewOrder = function(){
			employedWaiterService.makeNewOrder($scope.reservation, $scope.newOrder).then(
				function(response){
					$location.path('loggedIn/waiter/orders');
					findAll();
					
				}
			)
		}
		
		$scope.changeProfile = function(){
			employedWaiterService.changeProfile($scope.waiter.id,$scope.waiter).then(
				function(response){
					alert("successfully changed profile");
					$scope.state = undefined;
					findAll();
					$location.path('loggedIn/waiter/home');
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		
		$scope.changePassword = function(){
			employedWaiterService.changePassword($scope.waiter.id,$scope.waiter).then(
				function(response){
					alert("successfully changed password");
					$scope.state = undefined;
					findAll();
					$location.path('loggedIn/waiter/home');
				},
				function(response){
					alert("Error in changing");
				}
			);
		}
		
		
		
		$scope.loadTables = function(){
			employedWaiterService.restaurant().then(
					function(response){
						$scope.restaurant = response.data;
						
						employedWaiterService.allTables($scope.restaurant.id).then(
								function(response){
									
									employedWaiterService.tables().then(
											function(response2){
												$scope.waiterTables = response2.data;
											
											
												var stolovi = [];
												var red = [];
												var lastXPos = 0;
												var counter = 0;
												angular.forEach(response.data, function(value, key){	// punjenje matrice stolova
													angular.forEach(response2.data, function(value2, key2){
														if(value.id == value2.id){
															value.status = "Reserved";
														}
													});
													
													
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
											
											
											}
										);
							
			});
			
			
			
		});
		}
		
		$scope.changedShift = function(waiter) {
		    var today = Date.now();
		    var tomorrow = new Date(Date.now() + 86400000 * 1);
			var step = 2;
			var datesArr = [];
			var temp = 0;
			if(waiter.defaultShift != "First") 
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