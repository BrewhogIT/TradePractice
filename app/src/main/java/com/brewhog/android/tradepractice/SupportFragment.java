package com.brewhog.android.tradepractice;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

import androidx.fragment.app.Fragment;

public class SupportFragment extends Fragment {

    protected Drawable getDrawable(String path) throws IOException {
        AssetManager mAssetManager = getContext().getAssets();

        InputStream inputStream = mAssetManager
                .open(path);
        Drawable drawable = Drawable.createFromStream(inputStream,null);
        inputStream.close();
        return drawable;
    }

    protected Drawable getOptSizeImage (String path, View view) throws IOException {
        float viewHeight = view.getHeight();
        float viewWidth = view.getWidth();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        float imageHeight = options.outHeight;
        float imageWidth = options.outWidth;

        int inSampleSize = 1;
        if (imageHeight > viewHeight || imageWidth > viewWidth){
            float scaleHeight = imageHeight / viewHeight;
            float scaleWidth = imageWidth / viewWidth;

            inSampleSize = Math.round(Math.max(scaleHeight, scaleWidth));
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        AssetManager mAssetManager = getContext().getAssets();
        InputStream inputStream = mAssetManager
                .open(path);

        Bitmap drawable = BitmapFactory.decodeStream(inputStream,null,options);
        return new BitmapDrawable(getResources(),drawable);
    }
}
