package com.pokedesk.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.pokedesk.R;
import com.pokedesk.controllers.LoginController;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;

    private EditText etPassword;

    private Button btnLogin;

    private LoginController loginController;

    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
        initViewComponents();

    }

    private void initViewComponents() {

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etUsername.getText().toString())
                        && !TextUtils.isEmpty(etPassword.getText().toString())) {
                    loginController.login(etUsername.getText().toString(), etPassword.getText().toString());
                }
            }
        });

        String username = getSharedPreferences("poke", 0)
                .getString("username", null);

        String password = getSharedPreferences("poke", 0)
                .getString("password", null);

        if (username != null && password != null) {
            etUsername.setText(username);
            etPassword.setText(password);
        }


    }

    private void initComponents() {
        loginController = new LoginController(this);

    }


}
