<div>
  <form id="carform">
    <div class="form-group">
      <label for="year">Year:</label>
      <input class="form-control" type="number" name="year" id="year" />
    </div>
    <div class="form-group">
      <label for="make">Make:</label>
      <input class="form-control" type="text" name="make" id="make" />
    </div>
    <div class="form-group">
      <label for="model">Model:</label>
      <input class="form-control" type="text" name="model" id="model" />
    </div>
    <div>
      <label for="model">Trim Levels (1 per field):</label>
      <div id="trimlevels">
      </div>
      <br/>
      <button id="addtrimlevelbutton" class="btn btn-outline-dark float-right" type="button">Add Another Trim Level</button>
    </div>
    <br/>
    <br/>
    <br/>
    <hr/>
    <div class="form-group">
      <button type="button" class="btn btn-primary" id="submitcarbutton">Submit</button>
    </div>
  </form>
</div>

