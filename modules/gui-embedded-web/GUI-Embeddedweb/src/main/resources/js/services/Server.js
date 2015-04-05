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

  return instance;
});
