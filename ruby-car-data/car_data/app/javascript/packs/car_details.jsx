import React from 'react'
import ReactDOM from 'react-dom'

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
    var header = 'X-CSRF-Token';
    var token = $("meta[name='csrf-token']").attr("content");
    var carId = this.state.car.id;

    $("#deletecarbutton").prop("disabled", true);

    if (window.confirm("Delete this car?")) {
      var deleteCarRequest = $.ajax({
        url:"/api/cars/" + carId,
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
    fetch("/api/cars/" + window.location.pathname.split('/')[2])
      .then(
        (res) => {
          if (res.ok) {
            return res;
          } else {
            throw new Error(res.status + " " + res.statusText);
          }
        }
      )
      .then(res => res.json())
      .then(
        (result) => {
          this.process(result);
          this.setState({
            isLoaded: true,
            car: result
          });
        },
        // Note: it's important to handle errors here
        // instead of a catch() block so that we don't swallow
        // exceptions from actual bugs in components.
        (error) => {
          this.setState({
            isLoaded: true,
            error: error
          });
        }
      )
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
            <a id="editlink" className="btn btn-outline-dark btn-sm" href={car.id + "/edit"} role="button">Edit</a> <button onClick={this.handleClickDelete} className="btn btn-outline-dark btn-sm" id="deletecarbutton">Delete</button>
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