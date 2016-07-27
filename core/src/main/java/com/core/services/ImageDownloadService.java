package com.core.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.core.services.interfaces.IImageDownloadService;
import com.core.utils.Validation;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by oscargallon on 7/25/16.
 */

public class ImageDownloadService extends AsyncTask<String, Void, Boolean> {

    private IImageDownloadService iImageDownloadService;

    private String errorMessage;

    private Bitmap bitmap;

    private Context context;

    private String id;

    public ImageDownloadService(IImageDownloadService iImageDownloadService, Context context, String id) {
        this.iImageDownloadService = iImageDownloadService;
        this.context = context;
        this.id = id;
    }

    @Override
    protected Boolean doInBackground(String... url) {
        String urlString = url[0];
        if (!Validation.validateURl(urlString)) {
            errorMessage = "BAD URL";
            return false;

        }

        try {
            bitmap = Picasso.with(getContext()).load(urlString).get();
        } catch (IOException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBool) {
        super.onPostExecute(aBool);
        if (aBool && bitmap != null) {
            iImageDownloadService.onImageGot(bitmap, id);
            bitmap = null;
        } else {
            iImageDownloadService.onImageError(errorMessage);
        }
    }

    public Context getContext() {
        return context;
    }
}

