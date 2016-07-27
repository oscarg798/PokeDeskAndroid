package com.pokedesk.controllers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.core.model.utils.CoupleParams;
import com.core.services.HTTPServices;
import com.core.services.interfaces.IHTTPServices;
import com.pokedesk.R;
import com.pokedesk.controllers.abstracts.AbstractController;
import com.pokedesk.presentation.activities.MapActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscargallon on 7/25/16.
 */
public class LoginController extends AbstractController {
    public LoginController(Activity activity) {
        super(activity);
    }

    public void login(final String username, final String password) {

        List<CoupleParams> coupleParamsList = new ArrayList<>();

        coupleParamsList.add(new CoupleParams.CoupleParamBuilder("username")
                .nestedParam(username).createCoupleParam());

        coupleParamsList.add(new CoupleParams.CoupleParamBuilder("password")
                .nestedParam(password).createCoupleParam());

        HTTPServices httpServices = new HTTPServices(new IHTTPServices() {
            @Override
            public void successFullResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String token = jsonObject.getString("token");

                    getActivity()
                            .getSharedPreferences("poke", 0)
                            .edit().putString("username", username)
                            .putString("password", password)
                            .apply();

                    if (status.equals("success")) {
                        goToMapActivity(username, password, token);
                    } else {
                        Log.i("Error", "ERROR");
                        showAlertDialog("Error", "Please review your login's credentials. They must" +
                                "be your Pokemon Go linked Account");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showAlertDialog("Error", "Please review your login's credentials. They must" +
                            "be your Pokemon Go linked Account");
                }
                dismissProgressDialog();
            }

            @Override
            public void errorResponse(String message) {
                showAlertDialog("Error", "Please review your login's credentials. They must" +
                        "be your Pokemon Go linked Account");
                dismissProgressDialog();

            }
        }, coupleParamsList, "POST", false);

        httpServices.execute(getActivity()
                .getString(R.string.base_url) + getActivity()
                .getString(R.string.login_url));

        showProgressDialog("Login", "please wait");
    }


    public void goToMapActivity(String username, String password, String token) {
        Bundle b = new Bundle();
        b.putString("username", username);
        b.putString("token", token);
        b.putString("password", password);

        changeActivity(MapActivity.class, b);

    }
}
