var services = angular.module('bossManager.services', ['ngResource']);

var baseUrl = 'http://localhost\\:8080';

services.service('bossManagerService', ['$http', function($http){
	
	this.checkRights = function(){
		return $http.get("/bossManager/checkRights");
	}
	
	this.findBossManager = function(){
		return $http.get("/bossManager/boss");
	}
	
	this.findAllSystemManagers = function(){
		return $http.get("/bossManager");
	}
	
	this.findOne = function(id){
		return $http.get("/bossManager/"+id);
	}
	
	this.save = function(systemManager){
		return $http.post("/bossManager",systemManager);
	}
	
	this.updateBossMangerProfile = function(boss){
		return $http.put("/bossManager/"+boss.id,boss);
	}
}]);