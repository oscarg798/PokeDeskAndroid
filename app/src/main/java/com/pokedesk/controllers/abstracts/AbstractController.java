package com.pokedesk.controllers.abstracts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Stephys on 13/07/16.
 */
public abstract class AbstractController {

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

    public void changeActivity(Class<?> destinyClass, Bundle bundle) {
        Intent i = new Intent(getActivity(), destinyClass);
        i.putExtras(bundle);
        getActivity().startActivity(i);
    }

    public void showAlertDialog(String title, String message) {
        /**
         * Creamos el dialogo
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        /**
         * Le asignamos el titulo
         */
        builder.setTitle(title);

        /**
         * Le asignamos el mensaje
         */
        builder.setMessage(message);

        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.setCancelable(false);

        /**
         * Mostramos el dialogo
         */
        AlertDialog dialog = builder.show();
        dialog.show();

    }

    public void showAlertDialog(String title, String message,
                                DialogInterface.OnClickListener onCLickListenerPositiveButton,
                                String postiveButtonTitle) {

        /**
         * Creamos el dialogo
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        /**
         * Le asignamos el titulo
         */
        builder.setTitle(title);

        /**
         * Le asignamos el mensaje
         */
        builder.setMessage(message);

        builder.setPositiveButton(postiveButtonTitle, onCLickListenerPositiveButton);

        builder.setCancelable(false);

        /**
         * Mostramos el dialogo
         */
        AlertDialog dialog = builder.show();
        dialog.show();

    }

    /**
     * Metodo que muestra un dialog de texto
     *
     * @param title                         titulo del dialogo
     * @param message                       mensaje del dialogo
     * @param onCLickListenerPositiveButton accion del boton 1
     * @param onCLickListenerNegativeButton accion del boton 2
     * @param positiveButtonTitle           titulo boton 1
     * @param negativeButtonTitle           titulo boton 2
     */
    public void showAlertDialog(String title, String message,
                                DialogInterface.OnClickListener onCLickListenerPositiveButton,
                                DialogInterface.OnClickListener onCLickListenerNegativeButton,
                                String positiveButtonTitle, String negativeButtonTitle) {
        /**
         * Creamos el dialogo
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        /**
         * Le asignamos el titulo
         */
        builder.setTitle(title);

        /**
         * Le asignamos el mensaje
         */
        builder.setMessage(message);


        builder.setPositiveButton(positiveButtonTitle,
                onCLickListenerPositiveButton);

        if (negativeButtonTitle == null || onCLickListenerNegativeButton == null) {
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        } else {
            builder.setNegativeButton(negativeButtonTitle, onCLickListenerNegativeButton);
        }

        builder.setCancelable(false);

        /**
         * Mostramos el dialogo
         */
        AlertDialog dialog = builder.show();
        dialog.show();

    }

}
