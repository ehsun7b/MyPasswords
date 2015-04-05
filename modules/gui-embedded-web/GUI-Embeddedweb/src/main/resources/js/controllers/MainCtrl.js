"use strict";
var app = app || null;
app.controller("MainCtrl", function (/*$rootScope,*/ $scope, $location, $http, $log, localStorageService) {
  $log.debug("Main Controller");
  $scope.database = null;
  $scope.token = null;
  $scope.loggedIn = false;
  
  //if ($scope)
  
  
  
  $scope.fetchTokenAndDatabase = function() {
    $scope.token = localStorageService.get("token");
    $scope.database = localStorageService.get("database");
    $log.debug("token=" + $scope.token);
    $log.debug("database=" + $scope.database);
  };
  
  $scope.fetchTokenAndDatabase();
  
  if ($scope.token === null || $scope.database === null) {
    $location.path("/login");
  } else {
    $scope.loggedIn = true;
  }
});