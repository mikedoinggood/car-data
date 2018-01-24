angular.module('carDataApp', [
  'carList',
  'carForm',
  'carDetail',
  'ngResource',
]).
config(function($httpProvider) {
  $httpProvider.defaults.xsrfCookieName = 'csrftoken';
  $httpProvider.defaults.xsrfHeaderName = 'X-CSRFToken';
}).
factory('CarService', ['$resource',
  function($resource) {
    return $resource('/api/resources/cars/:carId', null,
      {
        'update': { method:'PUT' }
      });
  }
]);
