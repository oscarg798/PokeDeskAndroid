package com.pokedesk.applications;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by oscargallon on 7/25/16.
 */

public class PokeApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
