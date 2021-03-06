@extends('app')

@section('title', 'Car Data')

@section('head')
  <script type="text/javascript" src="/js/Chart.min.js"></script>
@endsection

@section('content')
  <div class="container" ng-app="carDataApp">
    <div id="charts" style="display:none;">
      <h4 class="text-center">Number Of Vehicles By Make</h4>
      <canvas id="makecountschart"></canvas>
      <hr/>
      <h4 class="text-center">Number Of Vehicles By Year</h4>
      <canvas id="yearcountschart"></canvas>
    </div>
  </div>
  <script type="text/javascript">
    var data;
    var labels = [];
    var counts = [];

    fetch("/resources/stats")
      .then(function(response) {
        return response.json();
      })
      .then(function(myJson) {
        document.getElementById("charts").style.display = "block";
        var makeCountsElement = document.getElementById("makecountschart");
        var yearCountsElement = document.getElementById("yearcountschart");
        createChart(myJson.makeCounts, makeCountsElement, false);
        createChart(myJson.yearCounts, yearCountsElement, true);
      });

    function createChart(data, element, labelsAreNumbersAsStrings) {
      var labels = [];
      var counts = [];
      var backgroundColors = [];
      var borderColors = [];

      for (var key in data) {
        labels.push(key);
        backgroundColors.push('rgba(54, 162, 235, 0.2)');
        borderColors.push('rgba(54, 162, 235, 1)');
      }

      if (labelsAreNumbersAsStrings) {
        labels.sort(function(a, b){return a-b});
      } else {
        labels.sort();
      }

      for (var i=0; i<labels.length; i++) {
        counts.push(data[labels[i]]);
      }

      var chart = new Chart(element, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [{
            label: 'Number of Vehicles',
            data: counts,
            backgroundColor: backgroundColors,
            borderColor: borderColors,
            borderWidth: 1
          }]
        },
        options: {
          legend: {
            display: false,
          },
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero:true
              }
            }]
          }
        }
      });
    }
  </script>
@endsection
