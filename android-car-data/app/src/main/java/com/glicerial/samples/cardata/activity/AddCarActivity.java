package com.glicerial.samples.cardata.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.glicerial.samples.cardata.R;
import com.glicerial.samples.cardata.model.Car;
import com.glicerial.samples.cardata.model.TrimLevel;
import com.glicerial.samples.cardata.network.CarClient;
import com.glicerial.samples.cardata.network.ServiceGenerator;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCarActivity extends AppCompatActivity {
    private String accessToken;
    private TextInputEditText yearEditText;
    private TextInputEditText makeEditText;
    private TextInputEditText modelEditText;
    private TextInputEditText trimLevelsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        accessToken = bundle.getString("access_token");

        yearEditText = (TextInputEditText) findViewById(R.id.year_input);
        makeEditText = (TextInputEditText) findViewById(R.id.make_input);
        modelEditText = (TextInputEditText) findViewById(R.id.model_input);
        trimLevelsEditText = (TextInputEditText) findViewById(R.id.trim_levels_input);

        //Toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        setTitle("Add Car");
    }

    // Referenced by button's onClick in layout xml file
    public void addCar(final View view) {
        // Build the car data
        Car car = new Car();
        car.setYear(Integer.valueOf(yearEditText.getText().toString()));
        car.setMake(makeEditText.getText().toString());
        car.setModel(modelEditText.getText().toString());

        Set<TrimLevel> trimLevelSet = new HashSet<TrimLevel>();
        String[] trimLevelsArray = trimLevelsEditText.getText().toString().split("\n");

        for (String trimLevelName : trimLevelsArray) {
            trimLevelSet.add(new TrimLevel(trimLevelName));
        }

        car.setTrimLevels(trimLevelSet);

        // Send the data
        CarClient carClient = ServiceGenerator.createService(CarClient.class);
        String authHeader = "Bearer " + accessToken;
        Call<Car> carRequest = carClient.addCar(authHeader, car);

        carRequest.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {

            }
        });
    }
}
