package com.glicerial.samples.cardata.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.glicerial.samples.cardata.R;
import com.glicerial.samples.cardata.network.AccessToken;
import com.glicerial.samples.cardata.network.AccessTokenClient;
import com.glicerial.samples.cardata.network.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (TextInputEditText) findViewById(R.id.username_input);
        passwordEditText = (TextInputEditText) findViewById(R.id.password_input);

        //Toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        setTitle("Login");

    }

    // Referenced by button's onClick in layout xml file
    public void login(final View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        AccessTokenClient tokenClient = ServiceGenerator.createService(AccessTokenClient.class);
        Call<AccessToken> tokenRequest = tokenClient.getPasswordGrantAccessToken(username, password, "password", "write", "android-cardata");

        tokenRequest.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                String accessToken = response.body().getAccessToken();
                Intent intent = new Intent();
                intent.putExtra("access_token", accessToken);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {

            }
        });
    }
}
