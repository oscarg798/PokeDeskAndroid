package com.pokedesk.services;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.pokedesk.R;
import com.pokedesk.services.interfaces.ISocketCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscargallon on 7/26/16.
 */

public class SocketService {

    private static SocketService instance;

    private Socket mSocket;

    private Context context;

    public static final String POKEMON_GOT_KEY = "pokeFound";

    public static final String INIT_KEY = "init";

    public static final String ERROR_KEY = "Error";

    public static final String AUTH_KEY = "auth";

    private List<ISocketCallback> iSocketCallbackList;

    private Handler handler;

    private SocketService() {

    }
    public static SocketService getInstance() {
        if (instance == null) {
            instance = new SocketService();
        }

        return instance;
    }

    public void registerSocketCallbacks(ISocketCallback iSocketCallback) {
        if (iSocketCallbackList == null) {
            iSocketCallbackList = new ArrayList<>();
        }
        iSocketCallbackList.add(iSocketCallback);
    }

    private void postSocketMessage(String key, JSONObject arg) {
        if (iSocketCallbackList == null) {
            return;
        }
        for (ISocketCallback iSocketCallback : iSocketCallbackList) {
            iSocketCallback.onSocketMessage(key, arg);
        }
    }

    private void postSocketMessage(String key, String arg) {
        if (iSocketCallbackList == null) {
            return;
        }
        for (ISocketCallback iSocketCallback : iSocketCallbackList) {
            iSocketCallback.onSocketMessage(key, arg);
        }
    }

    private Emitter.Listener onPokemonGot = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    JSONObject data = (JSONObject) args[0];
                    if (data != null) {
                        postSocketMessage(POKEMON_GOT_KEY, data);
                    } else {
                        sendGeneralSocketError();
                    }

                }
            });
        }
    };


    private Emitter.Listener init = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    JSONObject data = (JSONObject) args[0];
                    if (data != null) {
                        postSocketMessage(INIT_KEY, data);
                    } else {
                        sendGeneralSocketError();
                    }


                }
            });
        }
    };

    private Emitter.Listener auth = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.i("SOCKET SERVICE", "AUTH");
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String status = data.getString("status");
                        postSocketMessage(AUTH_KEY, status);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        sendGeneralSocketError();
                    }


                }
            });
        }
    };

    private Emitter.Listener error = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    JSONObject data = (JSONObject) args[0];
                    if (data != null) {
                        postSocketMessage(ERROR_KEY, data);
                    } else {
                        sendGeneralSocketError();
                    }


                }
            });
        }
    };

    private void sendGeneralSocketError() {
        postSocketMessage(ERROR_KEY, context.getString(R.string.socket_general_error));
    }

    private void runOnUiThread(Runnable r) {
        if (handler != null) {
            handler.post(r);
        }
    }



    public void setContext(Context context) {
        this.context = context;
        this.handler = new Handler(context.getMainLooper());
    }

    public boolean initSocket() {
        try {
            mSocket = IO.socket(context.getString(R.string.base_url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;

        }

        mSocket.connect();

        mSocket.on(POKEMON_GOT_KEY, onPokemonGot);

        mSocket.on(INIT_KEY, init);

        mSocket.on(AUTH_KEY, auth);

        mSocket.on(ERROR_KEY, error);



        return true;
    }

    public Socket getmSocket() {
        return mSocket;
    }
}
