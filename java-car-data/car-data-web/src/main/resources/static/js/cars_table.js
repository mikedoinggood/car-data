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
      cars: []
    };
    return _this;
  }

  _createClass(CarsTable, [{
    key: "process",
    value: function process(cars) {
      cars.map(function (car) {
        var trimLevelsArray = [];
        var trimLevelsString = "";
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
    key: "componentDidMount",
    value: function componentDidMount() {
      var request = $.ajax({
        url: "/resources/cars",
        method: "GET"
      });

      request.done(function (data) {
        this.process(data);
        this.setState({
          isLoaded: true,
          cars: data
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
          cars = _state.cars;

      if (error) {
        return React.createElement(
          "div",
          null,
          "Error: ",
          error.responseJSON.error
        );
      } else if (!isLoaded) {
        return React.createElement(
          "div",
          null,
          "Loading..."
        );
      } else {
        return React.createElement(
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
        );
      }
    }
  }]);

  return CarsTable;
}(React.Component);

var domContainer = document.querySelector('#cars_table_container');
ReactDOM.render(React.createElement(CarsTable, null), domContainer);