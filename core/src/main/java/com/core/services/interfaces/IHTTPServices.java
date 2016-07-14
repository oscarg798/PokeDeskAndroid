package com.core.services.interfaces;

import org.json.JSONObject;

/**
 * Created by oscargallon on 7/13/16.
 */

public interface IHTTPServices {

    void successFullResponse(String response);

    void errorResponse(String message);
}
