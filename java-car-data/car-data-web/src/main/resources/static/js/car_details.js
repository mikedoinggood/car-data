'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var CarDetails = function (_React$Component) {
  _inherits(CarDetails, _React$Component);

  function CarDetails(props) {
    _classCallCheck(this, CarDetails);

    var _this = _possibleConstructorReturn(this, (CarDetails.__proto__ || Object.getPrototypeOf(CarDetails)).call(this, props));

    _this.state = {
      error: null,
      isLoaded: false,
      car: {}
    };

    // This binding is necessary to make `this` work in the callback
    _this.handleClickDelete = _this.handleClickDelete.bind(_this);
    return _this;
  }

  _createClass(CarDetails, [{
    key: "process",
    value: function process(car) {
      car.trimLevels.sort(sortTrimLevels);
    }
  }, {
    key: "handleClickDelete",
    value: function handleClickDelete() {
      var header = $("meta[name='_csrf_header']").attr("content");
      var token = $("meta[name='_csrf']").attr("content");
      var carId = this.state.car.id;

      $("#deletecarbutton").prop("disabled", true);

      if (window.confirm("Delete this car?")) {
        var deleteCarRequest = $.ajax({
          url: "/resources/cars/" + carId,
          dataType: "json",
          method: "DELETE",
          beforeSend: function beforeSend(request) {
            return request.setRequestHeader(header, token);
          }
        });

        deleteCarRequest.done(function (data) {
          window.alert("Deleted.");
          window.location.href = "/";
        });

        deleteCarRequest.fail(function (data) {
          $("#deletecarbutton").prop("disabled", false);
          window.alert("Error deleting car!");
        });
      } else {
        $("#deletecarbutton").prop("disabled", false);
      }
    }
  }, {
    key: "componentDidMount",
    value: function componentDidMount() {
      var getCarRequest = $.ajax({
        url: "/resources/cars/" + window.location.pathname.split('/')[2],
        method: "GET"
      });

      getCarRequest.done(function (data) {
        this.process(data);
        this.setState({
          isLoaded: true,
          car: data
        });
      }.bind(this));

      getCarRequest.fail(function (data) {
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
          car = _state.car;

      if (error) {
        return React.createElement(
          "h4",
          null,
          "Car not found."
        );
      } else if (!isLoaded) {
        return React.createElement(
          "div",
          null,
          "Loading..."
        );
      } else {
        return React.createElement(
          "div",
          { id: "cardetails" },
          React.createElement(
            "div",
            null,
            React.createElement(
              "h4",
              null,
              "Car Details"
            ),
            React.createElement(
              "a",
              { id: "editlink", className: "btn btn-default btn-sm", href: car.id + "/edit", role: "button" },
              "Edit"
            ),
            " ",
            React.createElement(
              "button",
              { onClick: this.handleClickDelete, className: "btn btn-default btn-sm", id: "deletecarbutton" },
              "Delete"
            )
          ),
          React.createElement("hr", null),
          React.createElement(
            "div",
            null,
            React.createElement(
              "p",
              { className: "h4" },
              "Year:"
            ),
            React.createElement(
              "div",
              { className: "container" },
              React.createElement(
                "p",
                { id: "year" },
                car.year
              )
            )
          ),
          React.createElement(
            "div",
            null,
            React.createElement(
              "p",
              { className: "h4" },
              "Make:"
            ),
            React.createElement(
              "div",
              { className: "container" },
              React.createElement(
                "p",
                { id: "make" },
                car.make
              )
            )
          ),
          React.createElement(
            "div",
            null,
            React.createElement(
              "p",
              { className: "h4" },
              "Model:"
            ),
            React.createElement(
              "div",
              { className: "container" },
              React.createElement(
                "p",
                { id: "model" },
                car.model
              )
            )
          ),
          React.createElement(
            "div",
            null,
            React.createElement(
              "p",
              { className: "h4" },
              "Trim Levels:"
            ),
            React.createElement(
              "div",
              { className: "container", id: "trimlevels" },
              car.trimLevels.map(function (trim) {
                return React.createElement(
                  "div",
                  { key: trim.id },
                  trim.name
                );
              })
            )
          )
        );
      }
    }
  }]);

  return CarDetails;
}(React.Component);

var domContainer = document.querySelector('#car_details_container');
ReactDOM.render(React.createElement(CarDetails, null), domContainer);