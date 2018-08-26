'use strict';

class CarsTable extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      cars: []
    };
  }

  process(cars) {
    cars.map(car => {
      var trimLevelsArray = [];
      var trimLevelsString = "";
      car.trimLevels.sort(sortTrimLevels);

      for (var i=0; i<car.trimLevels.length; i++) {
        trimLevelsArray.push(car.trimLevels[i].name);
      }

      if (trimLevelsArray.length > 0) {
        car.trimLevelsString = trimLevelsArray.join(", ");
      }
    });
  }

  componentDidMount() {
	var request = $.ajax({
      url: "/resources/cars", 
      method: "GET",
    });

    request.done(function(data) {
      this.process(data);
      this.setState({
        isLoaded: true,
        cars: data
      });
    }.bind(this));

    request.fail(function(data) {
      this.setState({
        isLoaded: true,
        error: data
      })
    }.bind(this));
  }

  render() {
    const { error, isLoaded, cars } = this.state;
    if (error) {
      return <div>Error: {error.responseJSON.error}</div>;
    } else if (!isLoaded) {
      return <div>Loading...</div>;
    } else {
      return (
        <table className="table" id="carstable">
          <thead>
            <tr>
              <th>Year</th>
              <th>Make</th>
              <th>Model</th>
              <th>Trim Levels</th>
            </tr>
          </thead>
          <tbody id="tablebody">
            {cars.map(car => (
              <tr key={car.id}>
                <td><a href={"cars/" + car.id}>{car.year}</a></td>
                <td><a href={"cars/" + car.id}>{car.make}</a></td>
                <td><a href={"cars/" + car.id}>{car.model}</a></td>
                <td><a href={"cars/" + car.id}>{car.trimLevelsString}</a></td>
              </tr>
            ))}
          </tbody>
        </table>
      );
    }
  }
}

let domContainer = document.querySelector('#cars_table_container');
ReactDOM.render(<CarsTable />, domContainer);