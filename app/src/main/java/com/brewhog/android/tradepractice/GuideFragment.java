package com.brewhog.android.tradepractice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telecom.Call;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GuideFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<GuideItem> mGuideItemList;
    private String[] guideTexts;
    private int lessonKindLogo;
    private Callback mCallback;
    public static final String IMAGE_RES_ID_ARGS = "Resource id for lesson kind logo";


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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide,container,false);
        lessonKindLogo = getArguments().getInt(IMAGE_RES_ID_ARGS);

        mRecyclerView = view.findViewById(R.id.guide_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity(),RecyclerView.VERTICAL,false));

        guideTexts = getActivity().getResources().getStringArray(R.array.guide_text);
        mGuideItemList = new GuideItemPack().getInstruction();
        mRecyclerView.setAdapter(new GuideAdapter());
        return view;
    }

    private class GuideAdapter extends RecyclerView.Adapter<GuideHolder>{

        @NonNull
        @Override
        public GuideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            GuideHolder holder;

            //Создаем представление в зависимости от типа
            if (viewType == GuideItemPack.IMAGE_TYPE){
                holder = getImageHolder();
            }else {
                holder = getTextHolder();
            }

            return holder;
        }

        private int getPixFromDp(float i) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    i, getResources().getDisplayMetrics());
        }

        private GuideHolder getTextHolder() {
            GuideHolder holder;
            TextView textView = new TextView(getActivity());

            //конвертируем dp в пиксели
            int marginValue = getPixFromDp(4);

            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(marginValue,0,marginValue,marginValue);

            textView.setLayoutParams(params);
            textView.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            textView.setTextColor(getResources().getColor(R.color.coloVeryLightGray));

            holder = new TextHolder(textView);
            return holder;
        }

        private GuideHolder getImageHolder() {
            GuideHolder holder;
            ImageView imageView = new ImageView(getActivity());

            //конвертируем dp в пиксели
            int marginValue = getPixFromDp(4);
            int viewSize = getPixFromDp(250);

            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, viewSize);
            params.setMargins(0,0,0,marginValue);

            imageView.setLayoutParams(params);
            imageView.setBackgroundColor(getResources().getColor(R.color.colorLightGray));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            holder = new ImageHolder(imageView);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull GuideHolder holder, int position) {
            holder.Bind(mGuideItemList.get(position));
        }

        @Override
        public int getItemCount() {
            return mGuideItemList.size();
        }

        @Override
        public int getItemViewType(int position) {
            int viewType = mGuideItemList.get(position).getType();
            return viewType;
        }
    }

    private class GuideHolder extends RecyclerView.ViewHolder {
        public GuideHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void Bind(GuideItem item){

        }
    }

    private class TextHolder extends GuideHolder{
        private TextView mTextView;

        public TextHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = (TextView)itemView;
        }

        @Override
        public void Bind(GuideItem item){
            String text = guideTexts[item.getInfo()];
            mTextView.setText(text);
        }
    }

    private class ImageHolder extends GuideHolder {
        private ImageView mImageView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView;
        }

        @Override
        public void Bind(GuideItem item){
            int image;
            ImageView.ScaleType scaleType;

            //Первое изображение в списке - логотип типа урока на котором присутствует
            //анимация перехода по общему элементу
            if (getAdapterPosition() == 0){
                image = lessonKindLogo;
                scaleType = ImageView.ScaleType.FIT_XY;
                prepareForAnimationTransition(mImageView);
            }else{
                image = item.getInfo();
                scaleType  = ImageView.ScaleType.CENTER_INSIDE;
                //удаляем transitional name  т.к. холдер будет использоваться повторно, вместе с imageView
                mImageView.setTransitionName(null);
            }

            mImageView.setImageResource(image);
            mImageView.setScaleType(scaleType);
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
