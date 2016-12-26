'use strict';

angular.module('routerApp', ['ui.router', 
	'systemManager.services', 'systemManager.controllers', 
	'restaurantManager.services', 'restaurantManager.controllers',
	'bossManager.services', 'bossManager.controllers',
	'guest.services','guest.controllers',
	'bidder.services', 'bidder.controllers',
	'employedBartender.services','employedBartender.controllers',
	'employedCook.services','employedCook.controllers',
	'employedWaiter.services','employedWaiter.controllers',
	'bossManager.services', 'bossManager.controllers',
	'loginRegistration.services','loginRegistration.controllers'
	])
.config(function($stateProvider, $urlRouterProvider) {
        
        $urlRouterProvider.otherwise('/login');
        
        $stateProvider
        
        .state('login', {
        	url : '/login',
          	templateUrl : 'loginRegistration/login.html',
          	controller : 'loginRegistrationController'
         })
         
         .state('login.logOut', {
        	url : '/logout',
         	//templateUrl : 'loginRegistration/login.html',
         	resolve: {
        		promiseObj:  function($http,$location){
        			$location.path('login');
                    return $http.get("/commonController/logOut");
                 }}
         })
         
         .state('registration', {
        	url : '/registration',
        	templateUrl : 'loginRegistration/registration.html',
        	controller : 'loginRegistrationController'
         })
         
         .state('firstLogin', {
        	url : '/firstLogin',
        	templateUrl : 'loginRegistration/firstLogin.html',
        	controller : 'loginRegistrationController'
         })         
         
         .state('loggedIn', {
        	url : '/loggedIn',
        	templateUrl : 'loggedIn.html'
         })
         
        .state('loggedIn.home', {
        	url : '/home',
          	templateUrl : 'home.html'
         })
         
         .state('loggedIn.bossManager', {
        	url : '/bossManager',
          	templateUrl : 'managerBoss/bossManagerPartial.html',
            controller : 'bossManagerController'
         })
        .state('loggedIn.bossManager.list', {
        	url : '/list',
          	templateUrl : 'managerBoss/bossManagerList.html'
        })
         .state('loggedIn.bossManager.new', {
        	url : '/new',
        	templateUrl : 'managerBoss/bossManagerNew.html'
        })
         .state('loggedIn.bossManager.updateBossManagerProfile', {
			url : '/updateBossManagerProfile',
			templateUrl : 'managerBoss/bossManagerProfile.html'
		})
        
        .state('loggedIn.systemManager', {
        	url : '/systemManager',
          	templateUrl : 'managerSystem/systemManagerPartial.html',
            controller : 'systemManagerController'
        })
        .state('loggedIn.systemManager.list', {
        	url : '/list',
          	templateUrl : 'managerSystem/systemManagerList.html'
        })
        .state('loggedIn.systemManager.newRestaurantManager', {
        	url : '/newRestaurantManager',
        	templateUrl : 'managerSystem/systemManagerNewRestaurantManager.html'
        })
         .state('loggedIn.systemManager.newRestaurant', {
        	url : '/newRestaurant',
        	templateUrl : 'managerSystem/systemManagerNewRestaurant.html'
        })
        .state('loggedIn.systemManager.updateSystemManagerProfile', {
			url : '/updateSystemManagerProfile',
			templateUrl : 'managerSystem/systemManagerProfile.html'
		})
        
        
        .state('loggedIn.restaurantManager', {
        	url : '/restaurantManager',
          	templateUrl : 'managerRestaurant/restaurantManagerPartial.html',
            controller : 'restaurantManagerController'
         })
         .state('loggedIn.restaurantManager.info', {
        	url : '/info',
          	templateUrl : 'managerRestaurant/restaurantManagerInfo.html'
        })
        .state('loggedIn.restaurantManager.newDrink', {
			url : '/newDrink',
			templateUrl : 'managerRestaurant/restaurantManagerNewDrink.html'
        })
		.state('loggedIn.restaurantManager.newDish', {
			url : '/newDish',
			templateUrl : 'managerRestaurant/restaurantManagerNewDish.html'
		})
		.state('loggedIn.restaurantManager.newEmployed', {
			url : '/newEmpolyed',
			templateUrl : 'managerRestaurant/restaurantManagerNewEmployed.html'
		})
		.state('loggedIn.restaurantManager.newBidder', {
			url : '/newBidder',
			templateUrl : 'managerRestaurant/restaurantManagerNewBidder.html'
		})
		.state('loggedIn.restaurantManager.updateManagerProfile', {
			url : '/updateManagerProfile',
			templateUrl : 'managerRestaurant/restaurantManagerProfile.html'
		})
        
        
        .state('loggedIn.bartender', {
        	url: '/bartender',
        	templateUrl : 'employedBartender/employedBartenderPartial.html',
        	controller : 'employedBartenderController'
        })
        .state('loggedIn.bartender.home', {
        	url : '/home',
        	templateUrl : 'employedBartender/employedBartenderHome.html'
        })
        .state('loggedIn.bartender.orders', {
        	url : '/orders',
        	templateUrl : 'employedBartender/employedBartenderOrders.html'
        })
        .state('loggedIn.bartender.readyDrinks', {
        	url : '/readyDrinks',
        	templateUrl : 'employedBartender/employedBartenderReadyDrinks.html'
        })
        .state('loggedIn.bartender.profile', {
        	url : '/profile',
        	templateUrl : 'employedBartender/employedBartenderProfile.html'
        })
        
        
        
        .state('loggedIn.cook', {
        	url: '/cook',
        	templateUrl : 'employedCook/employedCookPartial.html',
        	controller : 'employedCookController'
        })
        .state('loggedIn.cook.home', {
        	url : '/home',
        	templateUrl : 'employedCook/employedCookHome.html'
        })
        .state('loggedIn.cook.orders', {
        	url : '/orders',
        	templateUrl : 'employedCook/employedCookOrders.html'
        })
         .state('loggedIn.cook.received', {
        	url : '/receivedFood',
        	templateUrl : 'employedCook/employedCookReceivedFood.html'
        })
        .state('loggedIn.cook.readyFood', {
        	url : '/readyFood',
        	templateUrl : 'employedCook/employedCookReadyFood.html'
        })
         .state('loggedIn.cook.profile', {
        	url : '/profile',
        	templateUrl : 'employedCook/employedCookProfile.html'
        })

        
        .state('loggedIn.waiter', {
        	url: '/waiter',
        	templateUrl : 'employedWaiter/employedWaiterPartial.html',
        	controller : 'employedWaiterController'
        })
        .state('loggedIn.waiter.home', {
        	url : '/home',
        	templateUrl : 'employedWaiter/employedWaiterHome.html'
        })
         .state('loggedIn.waiter.ready', {
        	url : '/ready',
        	templateUrl : 'employedWaiter/employedWaiterReadyOrder.html'
        })
         .state('loggedIn.waiter.readyOrders', {
        	url : '/readyOrders',
        	templateUrl : 'employedWaiter/employedWaiterOrders.html'
        })
        
        .state('loggedIn.guest', {
        	url: '/guest',
        	templateUrl : 'guestt/guestPartial.html',
        	controller : 'guestController'
        })
        .state('loggedIn.guest.home', {
        	url: '/home',
        	templateUrl : 'guestt/guestHome.html'
        })
        .state('loggedIn.guest.profile', {
        	url: '/profile',
        	templateUrl : 'guestt/guestProfile.html'
        })
        .state('loggedIn.notActivated', {
        	url: '/notActivated',
        	templateUrl : 'guestt/guestNotActivated.html'
        })
        .state('activation', {
        	url: '/activation/:acNum',
        	templateUrl: 'guestt/guestActivation.html',
        	resolve: {
        		promiseObj:  function($http, $stateParams){
                return $http.put("/guest/activate/"+ $stateParams.acNum);
             }}
        })
        .state('loggedIn.guest.updateGuestProfile', {
			url : '/updateGuestProfile',
			templateUrl : 'guestt/guestProfileUpdate.html'
		})
		.state('loggedIn.guest.friends', {
			url : '/friends',
			templateUrl : 'guestt/guestFriends.html'
			
		})
        
        
        
        .state('loggedIn.bidder', {
        	url: '/bidder',
        	templateUrl : 'bidder/bidderPartial.html',
            controller : 'bidderController'
        })
        .state('loggedIn.bidder.home', {
        	url: '/home',
        	templateUrl : 'bidder/bidderHome.html'
        })
        
        
        
});