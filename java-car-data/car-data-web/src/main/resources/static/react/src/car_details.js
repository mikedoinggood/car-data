'use strict';

class CarDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      car: {}
    };

    // This binding is necessary to make `this` work in the callback
    this.handleClickDelete = this.handleClickDelete.bind(this);
  }

  process(car) {
    car.trimLevels.sort(sortTrimLevels);
  }

  handleClickDelete() {
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    var carId = this.state.car.id;

    $("#deletecarbutton").prop("disabled", true);

    if (window.confirm("Delete this car?")) {
      var deleteCarRequest = $.ajax({
        url:"/resources/cars/" + carId,
        dataType: "json",
        method: "DELETE",
        beforeSend: function(request) {
          return request.setRequestHeader(header, token);
        }
      });

      deleteCarRequest.done(function(data) {
        window.alert("Deleted.");
        window.location.href="/";
      });

      deleteCarRequest.fail(function(data) {
        $("#deletecarbutton").prop("disabled", false);
          window.alert("Error deleting car!");
      });
    } else {
      $("#deletecarbutton").prop("disabled", false);
    }
  }

  componentDidMount() {
	var getCarRequest = $.ajax({
      url: "/resources/cars/" + window.location.pathname.split('/')[2],
      method: "GET",
    });

	getCarRequest.done(function(data) {
      this.process(data);
      this.setState({
        isLoaded: true,
        car: data
      });
    }.bind(this));

    getCarRequest.fail(function(data) {
      this.setState({
        isLoaded: true,
        error: data
      })
    }.bind(this));
  }

  render() {
    const { error, isLoaded, car } = this.state;
    if (error) {
      return <h4>Car not found.</h4>
    } else if (!isLoaded) {
      return <div>Loading...</div>;
    } else {
      return (
        <div id="cardetails">
          <div>
            <h4>Car Details</h4>
            <a id="editlink" className="btn btn-default btn-sm" href={car.id + "/edit"} role="button">Edit</a> <button onClick={this.handleClickDelete} className="btn btn-default btn-sm" id="deletecarbutton">Delete</button>
          </div>
          <hr/>
          <div>
            <p className="h4">Year:</p>
            <div className="container">
              <p id="year">{car.year}</p>
            </div>
          </div>
          <div>
            <p className="h4">Make:</p>
            <div className="container">
              <p id="make">{car.make}</p>
            </div>
          </div>
          <div>
            <p className="h4">Model:</p>
            <div className="container">
              <p id="model">{car.model}</p>
            </div>
          </div>
          <div>
            <p className="h4">Trim Levels:</p>
            <div className="container" id="trimlevels">
              {car.trimLevels.map(trim => (
                <div key={trim.id}>
                  {trim.name}
                </div>
              ))}
            </div>
          </div>
        </div>
      );
    }
  }
}

let domContainer = document.querySelector('#car_details_container');
ReactDOM.render(<CarDetails />, domContainer);