package ru.mininn.improvegroupetask;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SharedPreferencesHelper {
    private static final String IMAGE_PATH = "image";
    private static final String IMAGE_KEY = "image.png";
    private static final String PREFERENCES_KEY = "preferences";
    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final String PHONE_KEY = "phone";
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;

    private DiskLruCache mPhotoCache;

    //I know that this field can cause of memory leak but for so little app it's not critical.
    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        File cacheDir = context.getCacheDir();
        try {
            mPhotoCache = DiskLruCache.open(cacheDir, BuildConfig.VERSION_CODE , 1, DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public void saveUserInfo(String email, String phone, String password) {
        sharedPreferences.edit()
                .putString(EMAIL_KEY, email)
                .putString(PASSWORD_KEY, password)
                .putString(PHONE_KEY, phone)
                .apply();
    }

    public void clearData (Context context) {
        File cacheDir = new   File(context.getExternalCacheDir(), IMAGE_PATH);
        File cacheFile = new File(cacheDir, IMAGE_KEY);
        sharedPreferences.edit().remove(EMAIL_KEY).remove(PASSWORD_KEY).remove(PHONE_KEY).apply();
        cacheFile.delete();

    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL_KEY, null);
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD_KEY, null);
    }

    public String getPhone() {
        return sharedPreferences.getString(PHONE_KEY, null);
    }

    public boolean isUserExist(Context context) {
        return !(TextUtils.isEmpty(getPhone()) || TextUtils.isEmpty(getEmail()) ||
                TextUtils.isEmpty(getPassword()) || getPhotoBitmap(context) == null);
    }

    public void cachePhoto(Context context, Bitmap bitmap) {
        File cacheDir = new   File(context.getExternalCacheDir(), IMAGE_PATH);
        File cacheFile = new File(cacheDir, IMAGE_KEY);
        try {
            if (!cacheDir.isDirectory())
            cacheDir.mkdir();
            cacheFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(cacheFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e("image", "Error when saving image to cache. ", e);
        }
    }

    public Bitmap getPhotoBitmap(Context context) {
        File cacheDir = new   File(context.getExternalCacheDir(), IMAGE_PATH);
        File cacheFile = new File(cacheDir, IMAGE_KEY);
        try {
            FileInputStream fis = new FileInputStream(cacheFile);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public File getPhotoFile(Context context) {
        File cacheDir = new   File(context.getExternalCacheDir(), IMAGE_PATH);
        return new File(cacheDir, IMAGE_KEY);
    }


}
