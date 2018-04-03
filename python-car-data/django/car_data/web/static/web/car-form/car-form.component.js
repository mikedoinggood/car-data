angular.
  module('carForm').
  component('carForm', {
    templateUrl: '/static/web/car-form/car-form.template.html',
    controller: ['$window', 'CarService',
      function CarFormController($window, CarService) {
        var self = this;
        this.mode = $window.location.pathname.split('/')[3] == 'edit'? 'edit' : 'add';
        this.carId = $window.location.pathname.split('/')[2];

        if (this.mode == 'edit') {
          this.formHeader = 'Edit Car';

          CarService.get({carId: this.carId},
            function sucess(car) {
              self.showForm = true;
              self.car = car;

              if (self.car.trimLevels.length == 0) {
                self.addTrimLevel();
              }
            },
            function error() {
              self.carNotFound = true;
            }
          );
        } else {
          self.showForm = true;
          this.car = { trimLevels: [{name: ""}] };
          this.formHeader = 'Add Car';
        }

        this.addTrimLevel = function() {
          this.car.trimLevels.push({name: ""});
        }

        this.deleteTrimLevel = function(index) {
          this.car.trimLevels[index].delete = true;
        }

        this.submit = function(car) {
          self.submitDisabled = true;

          if (this.mode == 'edit') {
            // Check for updated trim level that's been changed to empty
            for (var i=0; i < car.trimLevels.length; i++) {
              if (car.trimLevels[i].id && !car.trimLevels[i].name) {
                $window.alert("Found a trim level that was changed to no value.\nPlease enter a value or delete the trim level.");
                self.submitDisabled = false;
                return;
              }
            }

            CarService.update({carId: this.carId}, car,
              function successCallBack() {
                $window.alert("Car updated.");
                $window.location.href = "/";
              },
              function errorCallBack() {
                $window.alert("Oops! Error trying to update car!");
                self.submitDisabled = false;
              }
            );
          } else {
            CarService.save(car,
              function successCallBack() {
                $window.alert("Car added.");
                $window.location.href = "/";
              },
              function errorCallBack() {
                $window.alert("Oops! Error trying to add car!");
                self.submitDisabled = false;
              }
            );
          }
        }
      }
    ]
  });