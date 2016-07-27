package com.pokedesk.services;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;

import com.pokedesk.R;

import java.util.List;

/**
 * Created by oscargallon on 7/25/16.
 */

public class PermissionProvider {

    private String rationalTitle = null;
    private String rationalMessage = null;
    private String rationalTxtBtn = null;


    public static final int REQUEST_PERMISSION_CODE = 1002;


    private static PermissionProvider instance = null;


    private PermissionProvider() {
    }

    public static PermissionProvider getInstance() {

        if (instance == null) {
            instance = new PermissionProvider();
        }
        return instance;

    }

    /**
     * Metodo para obtener el titulo para el mensaje
     * de explicacion de los permisos solicitados
     *
     * @return String
     */
    public String getRationalTitle() {
        return rationalTitle;
    }

    /**
     * Metodo para setear el titulo para el mensaje
     * de explicacion de los permisos solicitados
     *
     * @param rationalTitle
     */
    public void setRationalTitle(String rationalTitle) {
        this.rationalTitle = rationalTitle;
    }

    /**
     * Metodo para obtener la descripcion del mensaje de
     * explicacion de los permisos solicitados
     *
     * @return String
     */
    public String getRationalMessage() {
        return rationalMessage;
    }

    /**
     * Metodo para setear la descripcion del mensaje de
     * explicacion de los permisos solicitados
     *
     * @param rationalMessage
     */
    public void setRationalMessage(String rationalMessage) {
        this.rationalMessage = rationalMessage;
    }

    /**
     * Metodo para obtener el texto del boton del mensaje de
     * explicacion de los permisos
     *
     * @return String
     */
    public String getRationalTxtBtn() {
        return rationalTxtBtn;
    }

    /**
     * Metodo para setear el texto del boton del mensaje de
     * explicacion de los permisos
     *
     * @param rationalTxtBtn
     */
    public void setRationalTxtBtn(String rationalTxtBtn) {
        this.rationalTxtBtn = rationalTxtBtn;
    }

    /**
     * Metodo para chequear si la aplicacion cuenta con el permiso solicitado
     * retorna true en caso de que los tenga y false en caso de que no
     *
     * @param context
     * @param permission
     * @return boolean
     */
    public boolean checkPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * Este metodo muestra una alerta, en la cual se le realiza la solicitud del
     * permiso deseado al usuario
     *
     * @param activity   current activity
     * @param permission permission to request
     */
    private void showRequestPermissionDialog(Activity activity, String permission) {
        ActivityCompat.requestPermissions(activity,
                new String[]{permission},
                REQUEST_PERMISSION_CODE);
    }

    private void showRequestPermissionDialog(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity,
                permissions,
                REQUEST_PERMISSION_CODE);
    }

    /**
     * Este metodo verifica si se debe mostrar un mensaje de explicacion de la razon
     * por la cual se esta pidiendo el permiso en especifico, en caso de no ser necesario muestra
     * la alerta que solicita el permiso
     *
     * @param activity   the current activity
     * @param permission the permission to requests
     */
    public void askForPermissions(final Activity activity, final String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.PermissionDialog))
                            .setTitle(getRationalTitle())
                            .setMessage(getRationalMessage())
                            .setCancelable(false)
                            .setPositiveButton(getRationalTxtBtn(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showRequestPermissionDialog(activity, permission);
                                }
                            });

            Dialog dialog = builder.create();
            dialog.show();


        } else {
            showRequestPermissionDialog(activity, permission);
        }
    }

    public boolean mustShowRationalMessage(Activity activity, String[] permissions) {
        for (String s : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, s)) {
                return true;
            }

        }
        return false;
    }

    public void askForPermissions(final Activity activity, final String[] permissions) {
        if (mustShowRationalMessage(activity, permissions)) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.PermissionDialog))
                            .setTitle(getRationalTitle())
                            .setMessage(getRationalMessage())
                            .setCancelable(false)
                            .setPositiveButton(getRationalTxtBtn(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showRequestPermissionDialog(activity, permissions);
                                }
                            });

            Dialog dialog = builder.create();
            dialog.show();

        } else {
            showRequestPermissionDialog(activity, permissions);
        }

    }
}
