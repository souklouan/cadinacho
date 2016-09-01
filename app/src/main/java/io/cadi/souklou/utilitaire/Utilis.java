package io.cadi.souklou.utilitaire;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.cadi.souklou.ApplicationConstant;
import io.cadi.souklou.ApplicationContext;

/**
 * Created by arcadius on 08/08/16.
 */
public class Utilis {

    private static Context context = ApplicationContext.getInstance().getApplicationContext();


    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    //resize out of memory image
    public static void setSharePreference(String name, String value) {
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstant.SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (!preferences.contains(name)){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(name, value);
            editor.commit();
        } else
            Log.e("SharePreference", name+" already exist");
    }

    public static  String getSharePreference(String name) {
        SharedPreferences preferences = context.getSharedPreferences(ApplicationConstant.SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        String n = preferences.getString(name, "");
        return  (n.equals("")) ? null : n;
    }

    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(c.getTime());
    }
}
