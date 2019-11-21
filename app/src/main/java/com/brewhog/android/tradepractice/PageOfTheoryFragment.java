package com.brewhog.android.tradepractice;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PageOfTheoryFragment extends Fragment {
    public static final String ARG_LESSON_CONTENT_PATH = "lesson_content_path";
    public static final String ARG_PAGE_ILLUSTRATION_BYTE = "page_illustration_byte_array";
    private WebView lessonContentView;
    private ImageView pageIllustrationView;

public static PageOfTheoryFragment newInstance(String contentPath,byte[] illustrationByteArray) {
    Bundle args = new Bundle();
    args.putString(ARG_LESSON_CONTENT_PATH,contentPath);
    args.putByteArray(ARG_PAGE_ILLUSTRATION_BYTE,illustrationByteArray);

    PageOfTheoryFragment fragment = new PageOfTheoryFragment();
    fragment.setArguments(args);
    return fragment;
}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_of_theory,container,false);
        String contentPath = getArguments().getString(ARG_LESSON_CONTENT_PATH);
        byte[] illustrationByteArray = getArguments().getByteArray(ARG_PAGE_ILLUSTRATION_BYTE);
        Drawable illustrationDrawable = new BitmapDrawable(getResources(),
                BitmapFactory.decodeByteArray(illustrationByteArray,0,illustrationByteArray.length));

        pageIllustrationView = view.findViewById(R.id.page_illustration);
        pageIllustrationView.setImageDrawable(illustrationDrawable);
        lessonContentView = view.findViewById(R.id.page_content);
        lessonContentView.getSettings().setJavaScriptEnabled(true);
        lessonContentView.setWebViewClient(new WebViewClient());
        lessonContentView.loadUrl(contentPath);

        return view;
    }
}
