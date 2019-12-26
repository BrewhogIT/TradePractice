package com.brewhog.android.tradepractice;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class TestPageFragment extends Fragment {
    private static final String TAG = "TestPageFragment";
    private static final String ARG_LESSON_ID = "lesson_id";
    private static final String ARG_TEST_NUMBER = "test_number";
    private Test mTest;
    private Lesson mLesson;
    private ImageView questionIllustration;
    private TextView questionText;
    private LinearLayout answersField;
    private PagingSetting mPagingSetting;

    public static TestPageFragment newInstance(UUID lessonID, int testNumber, PagingSetting setting) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LESSON_ID,lessonID);
        args.putSerializable(ARG_TEST_NUMBER,testNumber);

        TestPageFragment fragment = new TestPageFragment(setting);
        fragment.setArguments(args);
        return fragment;
    }

    public TestPageFragment(PagingSetting pagingSetting) {
        mPagingSetting = pagingSetting;
    }

    public interface PagingSetting{
        void setPagingEnabled(boolean pagingEnabled);
        void nextPage();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UUID lessonID =(UUID) getArguments().getSerializable(ARG_LESSON_ID);
        int testNumber = getArguments().getInt(ARG_TEST_NUMBER);
        mLesson = LessonPack.getLessonPack(getActivity()).getLesson(lessonID);
        Log.i(TAG,"correct answer count is: " + mLesson.getCorrectAnswersCount());
        mTest = mLesson.getLessonTest().get(testNumber);

        View view = inflater.inflate(R.layout.fragment_test_page,container,false);
        questionIllustration = view.findViewById(R.id.question_image);
        questionText = view.findViewById(R.id.question_text);
        answersField = view.findViewById(R.id.answers_field);

        questionIllustration.setImageDrawable(getResources().getDrawable(R.drawable.test_backgroung));
        questionText.setText(mTest.getQuestion());

        createQuestionButtons(answersField);
        mPagingSetting.setPagingEnabled(false);
        return view;
    }

    private void createQuestionButtons(LinearLayout answersField){
        int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        int gravity = Gravity.CENTER_HORIZONTAL;

        //конвертируем dp в пиксели
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 10, getResources().getDisplayMetrics());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(matchParent,wrapContent);
        params.gravity = gravity;
        params.setMargins(0,0,0,value);
        Map<String,Boolean> answers = mTest.getAnswers();

        for (final Map.Entry<String,Boolean> pair : answers.entrySet()){
            Button button = new Button(getActivity());
            button.setText(pair.getKey());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String isCorrect = getResources().getString(R.string.incorrect);
                    if (pair.getValue()){
                        mLesson.setCorrectAnswersCount(mLesson.getCorrectAnswersCount() + 1);
                        isCorrect = getResources().getString(R.string.correct);
                    }

                    Snackbar.make(view,isCorrect,Snackbar.LENGTH_SHORT).show();
                    mPagingSetting.setPagingEnabled(true);
                    mPagingSetting.nextPage();
                }
            });

            answersField.addView(button,params);
        }
    }
}
