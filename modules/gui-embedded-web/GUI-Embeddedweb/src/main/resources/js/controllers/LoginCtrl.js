"use strict";
var app = app || null;
app.controller("LoginCtrl", function ($rootScope, $scope, $http, $log, Server, $location, localStorageService) {
  $log.debug("Login Controller");
  $scope.databases = [];
  $scope.messageType = null;
  $scope.message = null;
  $scope.password = null;

  $scope.getDatabases = function () {
    $log.debug("getting databases");
    var promise = Server.getDatabases();

    promise.then(function (data) {
      $log.debug(data);

      if (data.success) {
        $scope.databases = data.engines;
        $log.debug($scope.databases);
        if ($scope.databases.length === 1) {
          $scope.database = $scope.databases[0];
        }

        $scope.messageType = "info";
        $scope.message = data.successMessage;
      } else {
        $log.error(data.errorMessage);
        $scope.messageType = "error";
        $scope.message = data.errorMessage;
      }
    }, function (data) {
      $log.error(data);
    });
  };

  $scope.submit = function () {
    $log.debug("form submiting");
    $scope.message = "";
    
    if ($scope.password !== null) {
      var promise = Server.login($scope.database.engineName, $scope.password);
      promise.then(function (data) {
        if (data.success) {
          if (data.loginSuccess) {
            $log.debug(data);
            $location.path("/");
            localStorageService.set("token", data.token);
            localStorageService.set("database", $scope.database.engineName);
          } else {
            $scope.message = data.loginMessage;
            $scope.messageType = "warning";
          }
        } else {
          $scope.message = data.errorMessage;
          $scope.messageType = "error";
        }
      }, function (data) {
        $log.error(data);
        $scope.message = "Error!";
        $scope.messageType = "error";
      });
    } else {
      $scope.messageType = "warning";
      $scope.message = "Please enter password!";
    }
  };

  $scope.getDatabases();
});