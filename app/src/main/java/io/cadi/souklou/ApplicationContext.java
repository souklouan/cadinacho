package io.cadi.souklou;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by arcadius on 08/08/16.
 */
public class ApplicationContext {

    private final String SHARE_PREFERENCE_NAME = "cadi.io";

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

    public static void setSharePreference(Context context, String name, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARE_PREFERENCE_NAME, context.MODE_PRIVATE);
        if (!preferences.contains(name)){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(name,value);
            editor.apply();
        } else
            Log.e("SharePreference", name+" already exist");
    }

    public static  String getSharePreference(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(SHARE_PREFERENCE_NAME,context.MODE_PRIVATE);
        String n = preferences.getString(name, "");
        return  (!n.equalsIgnoreCase("")) ? null : n;
    }
}
