package com.brewhog.android.tradepractice;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

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
}
