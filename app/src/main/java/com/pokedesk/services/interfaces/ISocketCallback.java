package com.pokedesk.services.interfaces;

import org.json.JSONObject;

/**
 * Created by oscargallon on 7/26/16.
 */

public interface ISocketCallback {

    void onSocketMessage(String key, JSONObject arg);

    void onSocketMessage(String key, String message);

    void onSccketDissconnect();

}
