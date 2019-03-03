'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var CarsTable = function (_React$Component) {
  _inherits(CarsTable, _React$Component);

  function CarsTable(props) {
    _classCallCheck(this, CarsTable);

    var _this = _possibleConstructorReturn(this, (CarsTable.__proto__ || Object.getPrototypeOf(CarsTable)).call(this, props));

    _this.state = {
      error: null,
      isLoaded: false,
      cars: [],
      sort: null,
      page: 1,
      totalPages: 1
    };
    return _this;
  }

  _createClass(CarsTable, [{
    key: "process",
    value: function process(cars) {
      cars.map(function (car) {
        var trimLevelsArray = [];
        car.trimLevels.sort(sortTrimLevels);

        for (var i = 0; i < car.trimLevels.length; i++) {
          trimLevelsArray.push(car.trimLevels[i].name);
        }

        if (trimLevelsArray.length > 0) {
          car.trimLevelsString = trimLevelsArray.join(", ");
        }
      });
    }
  }, {
    key: "getSortOption",
    value: function getSortOption(value) {
      var sortOption;

      switch (value) {
        case "oldest":
          sortOption = "oldest";
          break;
        case "newest":
          sortOption = "newest";
          break;
        default:
          sortOption = "make";
      }

      this.setState({
        sort: sortOption
      });
    }
  }, {
    key: "handleSelect",
    value: function handleSelect(event) {
      switch (event.target.value) {
        case "make":
          window.location.href = "/";
          break;
        case "oldest":
          window.location.href = "?sort=oldest";
          break;
        case "newest":
          window.location.href = "?sort=newest";
          break;
      }
    }
  }, {
    key: "componentDidMount",
    value: function componentDidMount() {
      var requestUrl = "/resources/cars";
      var browserUrl = new URL(window.location.href);
      var sortParam = browserUrl.searchParams.get("sort");
      var pageParam = browserUrl.searchParams.get("page");

      requestUrl = pageParam ? requestUrl + "?page=" + pageParam : requestUrl + "?page=1";
      requestUrl = sortParam ? requestUrl + "&sort=" + sortParam : requestUrl;

      this.getSortOption(sortParam);

      var request = $.ajax({
        url: requestUrl,
        method: "GET"
      });

      request.done(function (data) {
        this.process(data.content);
        this.setState({
          isLoaded: true,
          cars: data.content,
          page: data.number + 1,
          totalPages: data.totalPages
        });
      }.bind(this));

      request.fail(function (data) {
        this.setState({
          isLoaded: true,
          error: data
        });
      }.bind(this));
    }
  }, {
    key: "render",
    value: function render() {
      var _state = this.state,
          error = _state.error,
          isLoaded = _state.isLoaded,
          cars = _state.cars,
          sort = _state.sort,
          page = _state.page,
          totalPages = _state.totalPages;


      if (error) {
        return React.createElement(
          "div",
          null,
          "Error trying to retrieve cars."
        );
      } else if (!isLoaded) {
        return React.createElement(
          "div",
          null,
          "Loading..."
        );
      } else if (cars.length == 0) {
        return React.createElement(
          "h4",
          null,
          "No cars found."
        );
      } else {
        var prev = void 0;
        var next = void 0;
        var params = sort == "make" ? "?page=" : "?sort=" + sort + "&page=";

        if (page < totalPages) {
          next = React.createElement(
            "span",
            null,
            React.createElement(
              "a",
              { href: params + (page + 1) },
              "[Next]"
            )
          );
        }

        if (page > 1) {
          prev = React.createElement(
            "span",
            null,
            React.createElement(
              "a",
              { href: params + (page - 1) },
              "[Previous]"
            )
          );
        }

        return React.createElement(
          "div",
          null,
          React.createElement(
            "div",
            null,
            React.createElement(
              "span",
              null,
              React.createElement(
                "label",
                { htmlFor: "sortselect" },
                "Sort by:\xA0"
              )
            ),
            React.createElement(
              "span",
              null,
              React.createElement(
                "select",
                { value: sort, id: "sortselect", onChange: this.handleSelect },
                React.createElement(
                  "option",
                  { value: "make" },
                  "Make"
                ),
                React.createElement(
                  "option",
                  { value: "oldest" },
                  "Oldest Year First"
                ),
                React.createElement(
                  "option",
                  { value: "newest" },
                  "Newest Year First"
                )
              )
            )
          ),
          React.createElement("br", null),
          React.createElement(
            "table",
            { className: "table", id: "carstable" },
            React.createElement(
              "thead",
              null,
              React.createElement(
                "tr",
                null,
                React.createElement(
                  "th",
                  null,
                  "Year"
                ),
                React.createElement(
                  "th",
                  null,
                  "Make"
                ),
                React.createElement(
                  "th",
                  null,
                  "Model"
                ),
                React.createElement(
                  "th",
                  null,
                  "Trim Levels"
                )
              )
            ),
            React.createElement(
              "tbody",
              { id: "tablebody" },
              cars.map(function (car) {
                return React.createElement(
                  "tr",
                  { key: car.id },
                  React.createElement(
                    "td",
                    null,
                    React.createElement(
                      "a",
                      { href: "cars/" + car.id },
                      car.year
                    )
                  ),
                  React.createElement(
                    "td",
                    null,
                    React.createElement(
                      "a",
                      { href: "cars/" + car.id },
                      car.make
                    )
                  ),
                  React.createElement(
                    "td",
                    null,
                    React.createElement(
                      "a",
                      { href: "cars/" + car.id },
                      car.model
                    )
                  ),
                  React.createElement(
                    "td",
                    null,
                    React.createElement(
                      "a",
                      { href: "cars/" + car.id },
                      car.trimLevelsString
                    )
                  )
                );
              })
            )
          ),
          React.createElement(
            "div",
            null,
            "Page ",
            page
          ),
          React.createElement(
            "div",
            null,
            prev,
            " ",
            next
          )
        );
      }
    }
  }]);

  return CarsTable;
}(React.Component);

var domContainer = document.querySelector('#cars_table_container');
ReactDOM.render(React.createElement(CarsTable, null), domContainer);