package com.core.services.interfaces;

import android.graphics.Bitmap;

/**
 * Created by oscargallon on 7/25/16.
 */

public interface IImageDownloadService {


    void onImageGot(Bitmap bitmap, String id);

    void onImageError(String err);
}
