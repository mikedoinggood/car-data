@extends('app')
  
@section('title', 'Add Car')

@section('content')
    <div class="container">
      <div class="col-md-8 offset-md-2 col-lg-5 offset-lg-3">
        <h3>Add Car</h3>
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

	    $("#submitcarbutton").click(function() {
                $("#submitcarbutton").prop("disabled", true);

                var car = {};
                car.year = $("#year").val();
                car.make = $("#make").val();
                car.model = $("#model").val();

                if ($(".trimlevel").length > 0) {
                    car.trimLevels = [];
    
                    $(".trimlevel").each(function() {
                        var trimLevel = {name: $(this).val()};
                        car.trimLevels.push(trimLevel);
                    });
                }

                var addCarRequest = $.ajax({
                    url: "/resources/cars",
                    method: "POST",
                    data: JSON.stringify(car),
                    contentType: "application/json; charset=UTF-8",
                    processData : false,
		    dataType: "json",
                    beforeSend: function(request) {
                      return request.setRequestHeader(header, token);
                  }
                });

                addCarRequest.done(function() {
                    alert("Car added!");
                    window.location.href = "/";
                });

                addCarRequest.fail(function(data) {
                    alert("Oops! Error trying to add car!");
                    $("#submitcarbutton").prop("disabled", false);
                });
            });
        });
    </script>
@endsection
