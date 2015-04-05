"use strict";
var app = angular.module("app", ["ngRoute"]);

app.config(function ($routeProvider, $locationProvider) {
  $routeProvider
          .when("/", {
            templateUrl: "html/home.html",
            controller: "HomeCtrl"
          })/*
          .when("/entry/:param", {
            templateUrl: "page/entry.html",
            controller: "EntryCtrl"
          })*/
          .otherwise({
            redirectTo: "/"
          });

  $locationProvider
          .html5Mode(false)
          .hashPrefix("!");

});


app.run(function ($rootScope, $interval, $window) {
  console.info("app run");

});
