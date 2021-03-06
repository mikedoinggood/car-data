@extends('app')
  
@section('title', 'Edit Car')

@section('content')
    <div class="container">
      <h4 id="carnotfound" style="display:none;">Car not found.</h4>
      <div id="editcar" style="display:none;" class="col-md-8 offset-md-2 col-lg-5 offset-lg-3">
        <h3>Edit Car</h3>
        <hr/>
        @include('car_form')
      </div>
    </div>
@endsection
@section('script')
    @include('car_form_script')
    <script type="text/javascript">
        $(document).ready(function() {
          var header = 'X-CSRF-Token';
          var token = $("meta[name='csrf-token']").attr("content");
          var id = window.location.pathname.split('/')[2];
          var car = {};
          car.trimLevels = [];

          var carDetailsRequest = $.ajax({
              url: "/resources/cars/" + id,
              method: "GET",
          });

          carDetailsRequest.done(function(data) {
              $("#editcar").show();
              $("#year").val(data.year);
              $("#make").val(data.make);
              $("#model").val(data.model);

              if (data.trim_levels.length > 0) {
                  data.trim_levels.sort(sortTrimLevels);
                  $.each(data.trim_levels, function(i, item) {
                      var div = $("<div class='input-group'>" +
                                    "<input class='form-control existingtrimlevel' type='text'/>" +
                                    "<span class='input-group-btn'>" +
                                      "<button id='trimlevel_" + item.id + "' type='button' class='btn btn-outline-dark'>Delete</button>" +
                                    "</span>" +
                                  "</div>");

                      div.find("input").val(item.name);
                      div.find("input").attr('id', "trimlevel_" + item.id);
                      div.find("button").click(function() {
                          $(this).parent().closest('div').hide();
                      });

                      $("#trimlevels").append(div);
                  });
              } else {
                  addNewTrimLevelInput();
              }
          });

          carDetailsRequest.fail(function(data) {
              $("#carnotfound").show();
          });

          $("#submitcarbutton").click(function() {
              $("#submitcarbutton").prop("disabled", true);
              car.year = $("#year").val();
              car.make = $("#make").val();
              car.model = $("#model").val();

              // Existing trim levels
              if ($(".existingtrimlevel").length > 0) {
                  var emptyExistingTrimLevel = false;

                  $(".existingtrimlevel").each(function() {
                      // Break loop if value is empty
                      if (!$(this).val().trim()) {
                          emptyExistingTrimLevel = true;
                          return false;
                      }

                      var trimLevelId = $(this).attr("id").split("_")[1];
                      var trimLevelData = {};

                      if ($(this).is(":visible")) {
                        trimLevelData = {id: trimLevelId, name:$(this).val()};
                      } else {
                        trimLevelData = {id: trimLevelId, name:$(this).val(), delete: true};
                      }

                      car.trimLevels.push(trimLevelData);
      	          });

		  if (emptyExistingTrimLevel) {
                      alert("Found a trim level that was changed to no value.\nPlease enter a value or delete the trim level.");
                      $("#submitcarbutton").prop("disabled", false);
                      return;
                  }
              }

              // New trim levels
              if ($(".trimlevel").length > 0) {
                  $(".trimlevel").each(function() {
                      var trimLevel = {name: $(this).val()};
                      car.trimLevels.push(trimLevel);
                  });
              }

              var editCarRequest = $.ajax({
                  url: "/resources/cars/" + id,
                  method: "PUT",
                  data: JSON.stringify(car),
                  contentType: "application/json; charset=UTF-8",
                  processData : false,
                  dataType: "json",
                  beforeSend: function(request) {
                      return request.setRequestHeader(header, token);
                  }
              });

              editCarRequest.done(function() {
                  alert("Car updated.");
                  window.location.href = "/";
              });

	      editCarRequest.fail(function(data) {
                  alert("Oops! Error trying to update car!");
                  $("#submitcarbutton").prop("disabled", false);
              });
          });
        });
    </script>
@endsection
