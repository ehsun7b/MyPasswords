"use strict";
var app = angular.module("app", ["ngRoute", "LocalStorageModule"]);

app.config(function ($routeProvider, $locationProvider, $logProvider) {
  $routeProvider
          .when("/", {
            templateUrl: "html/home.html",
            controller: "HomeCtrl"
          })/*
          .when("/entry/:param", {
            templateUrl: "page/entry.html",
            controller: "EntryCtrl"
          })*/
          .when("/search", {
            templateUrl: "html/search.html",
            controller: "SearchCtrl"
          })
          .when("/login", {
            templateUrl: "html/login.html",
            controller: "LoginCtrl"
          })
          .otherwise({
            redirectTo: "/"
          });

  $locationProvider
          .html5Mode(false)
          .hashPrefix("!");
  
  $logProvider.debugEnabled(true);

});


app.run(function ($rootScope, $interval, $window, $log) {
  $log.info("MyPasswords starts");
  $rootScope.logLevel = ["info", "log", "warn", "error"];
});
