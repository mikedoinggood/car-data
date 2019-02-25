angular.
  module('carList').
  component('carList', {
    templateUrl: '/static/web/car-list/car-list.template.html',
    controller: ['$filter', '$window', 'CarService',
      function CarListController($filter, $window, CarService) {
        var self = this;
        var browserUrl = new URL($window.location.href);
        var sortParam = browserUrl.searchParams.get("sort");
        var pageParam = browserUrl.searchParams.get("page");

        setupSortSelect();

        CarService.query({carId: null, sort: sortParam, page: pageParam},
          function success(response) {
            self.carsLoaded = true;
            self.cars = response.content;

            if (self.cars.length > 0) {
              self.page = response.number;
              self.carsFound = true;
              var pageLink = "";

              if (self.selectedSortOption.id == "newest" || self.selectedSortOption.id == "oldest") {
                pageLink += "?sort=" + self.selectedSortOption.id + "&page=";
              } else {
                pageLink += "?page=";
              }

              self.previousPageLink = response.number > 1 ? pageLink + (self.page - 1) : null;
              self.nextPageLink = response.number < response.totalPages ? pageLink + (self.page + 1) : null;
            } else {
              self.noCarsFound = true;
            }
          }, function error(response) {
            self.carsLoaded = true;
            self.noCarsFound = true;
          }
        );

        function setupSortSelect() {
          self.sortOptions = [
            {id: 'make', name: 'Make'},
            {id: 'oldest', name: 'Oldest Year First'},
            {id: 'newest', name: 'Newest Year First'},
          ];

          if (sortParam == "newest" || sortParam == "oldest") {
            for (var i=0; i < self.sortOptions.length; i++) {
              if (self.sortOptions[i].id == sortParam) {
                self.selectedSortOption = self.sortOptions[i];
                break;
              }
            }
          } else {
            self.selectedSortOption = self.sortOptions[0];
          }
        }

        this.getTrimLevels = function(trimLevels) {
          sortedTrimLevels = $filter('orderBy')(trimLevels, 'name');
          var output = [];

          angular.forEach(sortedTrimLevels, function(value, key) {
            this.push(value.name);
          }, output);

          return output.join(", ");
        }

        this.changeSort = function() {
          switch(this.selectedSortOption.id) {
            case "make":
              $window.location.href="/";
              break;
            case "oldest":
              $window.location.href="?sort=oldest"
              break;
            case "newest":
              $window.location.href="?sort=newest"
              break;
          }
        }
      }
    ]
  });