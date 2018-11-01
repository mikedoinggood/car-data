angular.
  module('carList').
  component('carList', {
    templateUrl: '/static/web/car-list/car-list.template.html',
    controller: ['$filter', 'CarService',
      function CarListController($filter, CarService) {
        var self = this;

        CarService.query(
          function success(response) {
            self.carsLoaded = true;
            self.cars = response;

            if (response.length > 0) {
              self.carsFound = true;
            } else {
              self.noCarsFound = true;
            }
          }, function error(response) {
            self.carsLoaded = true;
            self.noCarsFound = true;
          }
        );

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