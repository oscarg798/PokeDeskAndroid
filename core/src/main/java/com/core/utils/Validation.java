package com.core.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.webkit.URLUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by oscargallon on 7/13/16.
 */

public class Validation {

    /**
     * Expresion regular para validar emails
     */
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    private static final String LAT_PATTERN = "(-?[0-8]?[0-9](\\.\\d*)?)|-?90(\\.[0]*)?";

    private static final String LNG_PATTERN = "(-?([1]?[0-7][1-9]|[1-9]?[0-9])?(\\.\\d*)?)|-?180(\\.[0]*)?";

    /**
     * Validar que una url tenga un formato
     *
     * @param urlString String con la url a validar
     * @return verdadero de ser una url valida, falso de lo contrario
     */
    public static boolean validateURl(String urlString) {
        if (urlString == null)
            return false;
        if (TextUtils.isEmpty(urlString))
            return false;
        if (!URLUtil.isValidUrl(urlString))
            return false;

        return true;

    }

    /**
     * Metodo que valida si una fecha tiene el formato correcto
     *
     * @param fecha fecha a validar
     * @return verdadero si es correcto falso de lo contrario
     */
    public static boolean validateDate(String fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/mm/dd", Locale.getDefault());
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


    /**
     * Metedo para validar un email
     *
     * @param s email a validar
     * @return verdadero si tiene forma de email, falso de lo contrario
     */
    public static boolean validateEmail(String s) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();


    }

    /**
     * Metodo para ver si dos cadenas de texto son iguales
     *
     * @param s1 primer string
     * @param s2 segundo string
     * @return verdadero si los string son iguales falso de lo contrario
     */
    public static boolean compareTwoStrings(String s1, String s2) {
        if (s1.equals(s2))
            return true;
        return false;
    }

    public static boolean validateStringGretterThatLenght(String s1, int lenght) {
        if (s1 == null || s1.equals(""))
            return false;
        if (s1.length() < lenght)
            return false;

        return true;


    }

    /**
     * Metodo que valida un string que no puede ser vacio
     *
     * @param s string a validar
     * @return
     */
    public static boolean validateStringThanCanNotBeEmpty(String s) {
        if (s == null)
            return false;
        if (s.equals(""))
            return false;
        if (s.length() < 4)
            return false;
        return true;

    }

    public static boolean validateLat(String s) {
        Pattern pattern = Pattern.compile(LAT_PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public static boolean validateLng(String s) {
        Pattern pattern = Pattern.compile(LNG_PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    /**
     * metodo que valida un String que pueder ser vacio
     *
     * @param s string a validar
     * @return
     */
    public static boolean validateStringThatCanBeEmpty(String s) {
        if (s == null)
            return false;
        return true;
    }

    /**
     * Valida que el nombre de usuario  tenga la longitud correcta
     *
     * @param s contraseÃ±a a validar
     * @return verdarero si tiene la longitud adecuada falso de lo contrario
     */
    public static boolean validateUsername(String s) {
        if (TextUtils.isEmpty(s))
            return false;
        if (s.length() < 5)
            return false;

        return true;
    }

    /**
     * Valida que la contraseÃ±a  tenga la longitud correcta
     *
     * @param s contraseÃ±a a validar
     * @return verdarero si tiene la longitud adecuada falso de lo contrario
     */
    public static boolean validatePassword(String s) {
        if (TextUtils.isEmpty(s))
            return false;
        if (s.length() < 6)
            return false;

        return true;
    }

    /**
     * Valida si un string puede tener numeros
     *
     * @param s string a validar
     * @return verdadero si la cadena de texto son solo numero
     * falso de lo contrario
     */
    public static boolean validateStringWithNumbers(String s) {
        if (s.equals(""))
            return false;
        return TextUtils.isDigitsOnly(s);
    }

    public static boolean checkConn(Activity activity) {
        ConnectivityManager conMgr = (ConnectivityManager)
                activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;

    }
}
