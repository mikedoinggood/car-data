angular.
  module('carList').
  component('carList', {
    templateUrl: 'static/web/car-list/car-list.template.html',
    controller: ['$http', '$filter',
      function CarListController($http, $filter) {
        var self = this;

        $http.get("/api/resources/cars")
          .then(function (response) {
            self.cars = response.data;
          });

        this.getTrimLevels = function(trimLevels) {
          sortedTrimLevels = $filter('orderBy')(trimLevels, 'name');
          var output = [];

          angular.forEach(sortedTrimLevels, function(value, key) {
            this.push(value.name);
          }, output);

          return output.join(", ");
        }
      }
    ]
  });