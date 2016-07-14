package com.pokedesk.controllers.abstracts;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

/**
 * Created by Stephys on 13/07/16.
 */
public abstract  class AbstractController {

    private final Activity activity;

    /**
     * Barra de progreso
     */
    private ProgressDialog progressDialog;

    public AbstractController(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void showProgressDialog(String title, String message, boolean cancelable) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();

    }

    /**
     * Metodo que muestra un dialogo de progreso
     *
     * @param title   titulo del dialogo
     * @param message mensaje del dialogo
     */
    public void showProgressDialog(String title, String message) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();

    }

    /**
     * Metodo que oculta un dialogo de progreso
     */
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }


    public void changeActivity(Class<?> destinyClass) {
        Intent i = new Intent(getActivity(), destinyClass);
        getActivity().startActivity(i);

    }

}
