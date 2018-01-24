angular.
  module('carDetail').
  component('carDetail', {
    templateUrl: '/static/web/car-detail/car-detail.template.html',
    controller: ['$window', 'CarService',
      function CarDetailController($window, CarService) {
        var self = this;
        this.carId = $window.location.pathname.split('/')[2];

        this.car = CarService.get({carId: this.carId},
          function sucess(car) {
            self.carFound = true;
          },
          function error() {
            self.carNotFound = true;
          }
        );

        this.confirmDelete = function () {
          this.deleteDisabled = true;

          if ($window.confirm("Delete this car?")) {
            CarService.delete({carId: this.carId},
              function success(car) {
                $window.alert("Deleted.");
                $window.location.href = '/';
              },
              function error() {
                self.deleteDisabled = false;
                $window.alert("Error deleting car!");
              }
            );
          } else {
            this.deleteDisabled = false;
          }
        }
      }
    ]
  });