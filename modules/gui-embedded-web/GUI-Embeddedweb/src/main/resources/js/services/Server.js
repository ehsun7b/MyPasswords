"use strict";
app.factory("Server", function ($http, $q) {
  var instance = {};

  instance.getDatabases = function () {
    var deferred = $q.defer();

    $http({method: "GET", url: "/database"}).
            success(function (data) {
              deferred.resolve(data);
            }).
            error(function (data) {
              deferred.reject(data);
            });

    return deferred.promise;
  };

  instance.login = function (engine, password) {
    var deferred = $q.defer();

    $http({method: "POST",
      url: "/login",
      data: {password: password, engine: engine},
      headers: {'Content-Type': 'application/json; charset=utf-8'}
    }
    ).success(function (data, status, headers) {
      var token = headers("token");
      data.token = token;
      deferred.resolve(data);
    }).error(function (data) {
      deferred.reject(data);
    });

    return deferred.promise;
  };



  return instance;
});
