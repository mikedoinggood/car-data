angular.
  module('carForm').
  config(function($httpProvider) {
    $httpProvider.defaults.xsrfCookieName = 'csrftoken';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
  }).
  component('carForm', {
    templateUrl: 'static/web/car-form/car-form.template.html',
    controller: ['$http', '$window',
      function CarListController($http, $window) {
        this.submit = function(car) {
          var self = this;
          self.submitDisabled = true;

          var carToSubmit = angular.copy(car);

          if (car.trimLevels) {
              var trimLevelsNameArray = car.trimLevels.split("\n");
              var trimLevelsObjectArray = [];

              angular.forEach(trimLevelsNameArray, function(value, key) {
                this.push({"name": value});
              }, trimLevelsObjectArray);

              carToSubmit.trimLevels = trimLevelsObjectArray;
          }

          $http.post("/api/resources/cars", carToSubmit).
            then(
            function successCallBack() {
              $window.alert("Car Added!");
              $window.location.href = "/";
            },
            function errorCallBack() {
              $window.alert("Oops! Error trying to add car!");
              self.submitDisabled = false;
            });
        }
      }
    ]
  });