package com.glicerial.samples.cardata.activity;


import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.glicerial.samples.cardata.R;
import com.glicerial.samples.cardata.model.Car;
import com.glicerial.samples.cardata.model.TrimLevel;
import com.glicerial.samples.cardata.network.CarClient;
import com.glicerial.samples.cardata.network.ServiceGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String accessToken;
    private TextView carsTextView;
    private Button launchLoginActivityButton;
    private static final int LOGIN = 1;
    private static final int ADD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carsTextView = (TextView) findViewById(R.id.cars_textview);
        launchLoginActivityButton = (Button) findViewById(R.id.launch_login_activity_button);

        //Toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        setTitle("Car Data");

        // For debugging, we set retrofit's HTTP logging, sets an interceptor, we want this to be last intercepter added
        ServiceGenerator.enableHttpLogging();

        if (accessToken == null) {
            launchLoginActivityButton.setVisibility(View.VISIBLE);
        } else {
            fetchCars();
        }
    }

    private void fetchCars() {
        CarClient client = ServiceGenerator.createService(CarClient.class);
        String authHeader = "Bearer " + accessToken;
        Call<List<Car>> carsCall = client.getCars(authHeader);

        carsCall.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                List<Car> cars = response.body();

                if (cars != null) {
                    StringBuilder sb = new StringBuilder();

                    for (Car c : cars) {
                        sb.append(c.getYear() + " " + c.getMake() + " " + c.getModel());
                        sb.append("\nTrim Levels:\n");

                        List<TrimLevel> trimLevelList = new ArrayList<TrimLevel>(c.getTrimLevels());
                        Collections.sort(trimLevelList, new Comparator<TrimLevel>() {
                            @Override
                            public int compare(TrimLevel t1, TrimLevel t2) {
                                return t1.getName().toLowerCase().compareTo(t2.getName().toLowerCase());
                            }
                        });

                        for (TrimLevel t : trimLevelList) {
                            sb.append(t.getName() + "\n");
                        }

                        sb.append("\n");
                    }

                    carsTextView.setText(sb.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.add:
                if (accessToken == null) {
                    Snackbar.make(carsTextView, "Please Login", Snackbar.LENGTH_LONG).show();
                } else {
                    intent = new Intent(this, AddCarActivity.class);
                    intent.putExtra("access_token", accessToken);
                    startActivityForResult(intent, ADD);
                }

                return true;
            case R.id.refresh:
                if (accessToken == null) {
                    Snackbar.make(carsTextView, "Please Login", Snackbar.LENGTH_LONG).show();
                } else {
                    fetchCars();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case (LOGIN) :
                if (resultCode == Activity.RESULT_OK) {
                    launchLoginActivityButton.setVisibility(View.GONE);
                    accessToken = data.getStringExtra("access_token");
                    fetchCars();
                }
                break;
            case (ADD) :
                if (resultCode == Activity.RESULT_OK) {
                    fetchCars();
                }
                break;
        }
    }

    // Referenced by button's onClick in layout xml file
    public void launchLoginActivity(final View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN);
    }
}
