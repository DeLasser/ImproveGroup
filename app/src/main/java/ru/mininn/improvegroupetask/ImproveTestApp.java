package ru.mininn.improvegroupetask;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class ImproveTestApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}