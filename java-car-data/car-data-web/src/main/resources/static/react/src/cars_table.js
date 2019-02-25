'use strict';

class CarsTable extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      cars: [],
      sort: null,
      page: 1,
      totalPages: 1
    };
  }

  process(cars) {
    cars.map(car => {
      var trimLevelsArray = [];
      car.trimLevels.sort(sortTrimLevels);

      for (var i=0; i<car.trimLevels.length; i++) {
        trimLevelsArray.push(car.trimLevels[i].name);
      }

      if (trimLevelsArray.length > 0) {
        car.trimLevelsString = trimLevelsArray.join(", ");
      }
    });
  }

  getSortOption(value) {
    var sortOption;

    switch (value) {
      case "oldest":
        sortOption = "oldest"
        break;
      case "newest":
        sortOption = "newest";
        break;
      default:
        sortOption = "make";
    }

    this.setState({
      sort: sortOption
    })
  }

  handleSelect(event) {
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

  componentDidMount() {
    var requestUrl = "/resources/cars";
    var browserUrl = new URL(window.location.href);
    var sortParam = browserUrl.searchParams.get("sort");
    var pageParam = browserUrl.searchParams.get("page");

    requestUrl = pageParam ? requestUrl + "?page=" + pageParam : requestUrl + "?page=1";
    requestUrl = sortParam ? requestUrl + "&sort=" + sortParam : requestUrl;

    this.getSortOption(sortParam);

    var request = $.ajax({
      url: requestUrl,
      method: "GET",
    });

    request.done(function(data) {
      this.process(data.content);
      this.setState({
        isLoaded: true,
        cars: data.content,
        page: data.number + 1,
        totalPages: data.totalPages
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
    const { error, isLoaded, cars, sort, page } = this.state;

    if (error) {
      return <div>Error: {error.responseJSON.error}</div>;
    } else if (!isLoaded) {
      return <div>Loading...</div>;
    } else if (cars.length == 0) {
      return <h4>No cars found.</h4>;
    } else {
      let prev;
      let next;
      let params = this.state.sort == "make" ? "?page=" : "?sort=" + this.state.sort + "&page=";

      if (this.state.page < this.state.totalPages) {
        next = <span><a href={params + (this.state.page + 1)}>[Next]</a></span>;
      }

      if (this.state.page > 1) {
        prev = <span><a href={params + (this.state.page - 1)}>[Previous]</a></span>;
      }

      return (
        <div>
          <div>
            <span>
              <label for="sortselect">Sort by:&nbsp;</label>
            </span>
            <span>
              <select value={this.state.sort} id="sortselect" onChange={this.handleSelect}>
                <option value="make">Make</option>
                <option value="oldest">Oldest Year First</option>
                <option value="newest">Newest Year First</option>
              </select>
            </span>
          </div>
          <br/>
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
          <div>Page {this.state.page}</div>
          <div>{prev} {next}</div>
        </div>
      );
    }
  }
}

let domContainer = document.querySelector('#cars_table_container');
ReactDOM.render(<CarsTable />, domContainer);