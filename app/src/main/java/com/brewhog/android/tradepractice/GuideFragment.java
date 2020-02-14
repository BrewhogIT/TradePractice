package com.brewhog.android.tradepractice;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GuideFragment extends Fragment {
    private final static String TAG = "GuideFragment";
    public static final String IMAGE_RES_ID_ARGS = "Resource id for lesson kind logo";
    private final static int IMAGE_TYPE = 0;
    private final static int WEB_TYPE = 1;
    private RecyclerView mRecyclerView;
    private int lessonKindLogo;
    private Callback mCallback;
    private int orientation;


    public static GuideFragment newInstance(int imageResId) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_RES_ID_ARGS, imageResId);

        GuideFragment fragment = new GuideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //Нужен для возобновления анимации перехода между общим элементом
    public interface Callback{
        void setStopPostTransition();
        void setStartPostTransition();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (Callback)getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //останавливаем анимацию перехода по общему элементу
        mCallback.setStopPostTransition();
        orientation = getResources().getConfiguration().orientation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide,container,false);
        lessonKindLogo = getArguments().getInt(IMAGE_RES_ID_ARGS);

        int managerOrientation = (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                RecyclerView.VERTICAL : RecyclerView.HORIZONTAL;
        RecyclerView.LayoutManager manager = new LinearLayoutManager
                (getActivity(),managerOrientation,false){
            //При альбомной ориентации отключаем возможность листать RecyclerView
            // т.к. оба представления занимают всю площадь экрана
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };

        mRecyclerView = view.findViewById(R.id.guide_recycler_view);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(new GuideAdapter());

//        LayoutAnimationController animationController = AnimationUtils
//                .loadLayoutAnimation(getActivity(),R.anim.layout_animation_scale);
//        mRecyclerView.setLayoutAnimation(animationController);
        return view;
    }

    private class GuideAdapter extends RecyclerView.Adapter<GuideHolder>{
        int fixWidth = getPixFromDp(400);
        int fixHeight = getPixFromDp(250);

        @NonNull
        @Override
        public GuideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            GuideHolder holder;

            //Создаем представление в зависимости от типа
            if (viewType == IMAGE_TYPE){
                holder = getImageHolder();
            }else {
                holder = getWebHolder();
            }

            return holder;
        }

        private int getPixFromDp(float i) {
            //конвертируем пиксели в дип
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    i, getResources().getDisplayMetrics());
        }

        private GuideHolder getWebHolder() {
            GuideHolder holder;
            WebView webView = new WebView(getActivity());

            //конвертируем dp в пиксели
            int marginValue = getPixFromDp(4);
            int bottomMargin = getPixFromDp(8);

            int viewWidth;
            int viewHeight;
            if (orientation == Configuration.ORIENTATION_PORTRAIT){
                viewWidth = ViewGroup.LayoutParams.MATCH_PARENT;
                viewHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
            }else{
                //В альбомной ориентации ImageView и WebView должны помещаться на одном экране
                //поэтому рассчитываем ширину WebView исходя из свободного места на экране
                int activityWidth = getResources().getConfiguration().screenWidthDp;
                viewWidth = getPixFromDp(activityWidth) - fixWidth;
                viewHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams
                    (viewWidth, viewHeight);
            params.setMargins(marginValue,0,marginValue,bottomMargin);

            webView.setLayoutParams(params);
            webView.setBackgroundColor(getResources().getColor(R.color.colorVeryLightGray));

            WebSettings setting = webView.getSettings();
            setting.setDefaultTextEncodingName("utf-8");
            webView.setWebViewClient(new WebViewClient());

            holder = new WebHolder(webView);
            return holder;
        }

        private GuideHolder getImageHolder() {
            GuideHolder holder;
            ImageView imageView = new ImageView(getActivity());

            int marginValue = getPixFromDp(4);

            int viewWidth;
            int viewHeight;
            if (orientation == Configuration.ORIENTATION_PORTRAIT){
                viewWidth = ViewGroup.LayoutParams.MATCH_PARENT;
                viewHeight = fixHeight;
            }else{
                viewWidth = fixWidth;
                viewHeight = ViewGroup.LayoutParams.MATCH_PARENT;
            }

            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams
                    (viewWidth, viewHeight);
            params.setMargins(0,0,0,marginValue);

            imageView.setLayoutParams(params);
            imageView.setBackgroundColor(getResources().getColor(R.color.colorVeryLightGray));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            holder = new ImageHolder(imageView);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull GuideHolder holder, int position) {
            holder.Bind();
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return (position == 0? IMAGE_TYPE : WEB_TYPE);
        }
    }

    private abstract class GuideHolder extends RecyclerView.ViewHolder {
        public GuideHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void Bind();
    }

    private class WebHolder extends GuideHolder{
        private WebView mWebView;

        public WebHolder(@NonNull View itemView) {
            super(itemView);
            mWebView = (WebView)itemView;
        }

        @Override
        public void Bind(){
            String text = fetchHTML("guide/guide.html");
            mWebView.loadDataWithBaseURL(
                    null,
                    text,
                    "text/html; charset=utf-8",
                    "utf-8",
                    null);

            Animation slideFromBottomAnim = AnimationUtils
                    .loadAnimation(getActivity(),R.anim.item_animation_from_bottom);
            mWebView.setAnimation(slideFromBottomAnim);
        }

        public String fetchHTML(String fileName) {
            StringBuilder sb = new StringBuilder();
            String tmpStr = "";
            try {
                InputStream in = getActivity().getAssets().open(fileName);
                BufferedReader bfr = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                while ((tmpStr = bfr.readLine()) != null) {
                    sb.append(tmpStr);
                }
                in.close();
            } catch (IOException e) {
                Log.e(TAG,"error in guide file reading");
                e.printStackTrace();
            }
            return sb.toString();
        }
    }

    private class ImageHolder extends GuideHolder {
        private ImageView mImageView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView;
        }

        @Override
        public void Bind(){
            int image = lessonKindLogo;

            //изображение в списке - логотип типа урока на котором присутствует
            //анимация перехода по общему элементу
            prepareForAnimationTransition(mImageView);
            mImageView.setImageResource(image);
        }

        private void prepareForAnimationTransition(final View view) {
            view.setTransitionName(
                    getActivity().getString(R.string.lesson_kind_logo_transitional_name));

            //Так как анимация была остановлена в onCreate ее нужно восстановить
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    mCallback.setStartPostTransition();
                    return false;
                }
            });
        }
    }

}
