package com.brewhog.android.tradepractice;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class TestPageFragment extends Fragment {
    private static final String TAG = "TestPageFragment";
    private static final String ARG_LESSON_ID = "lesson_id";
    private static final String ARG_TEST_NUMBER = "test_number";
    private Test mTest;
    private Lesson mLesson;
    private ImageView questionIllustration;
    private TextView questionText;
    private LinearLayout answersField;
    private CustomViewPager mCustomViewPager;
    private int lessonID;
    private int testNumber;
    private CallBack mCallBack;

    public static TestPageFragment newInstance(int lessonID, int testNumber) {
        Bundle args = new Bundle();
        args.putInt(ARG_LESSON_ID,lessonID);
        args.putInt(ARG_TEST_NUMBER,testNumber);

        TestPageFragment fragment = new TestPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TestPageFragment(){
        super();
    }

    //необходим, для обновления ссылки на пейджер незаисимо от активности(например при смене конфигурации)
    public interface CallBack{
        View getPager();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallBack = (CallBack) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCustomViewPager = (CustomViewPager) mCallBack.getPager();

        lessonID = getArguments().getInt(ARG_LESSON_ID);
        testNumber = getArguments().getInt(ARG_TEST_NUMBER);

        mLesson = LessonPack.getLessonPack(getActivity()).getLesson(lessonID);
        Log.i(TAG,"correct answer count is: " + mLesson.getCorrectAnswersCount());
        mTest = mLesson.getLessonTest().get(testNumber);

        View view = inflater.inflate(R.layout.fragment_test_page,container,false);
        questionIllustration = view.findViewById(R.id.question_image);
        questionText = view.findViewById(R.id.question_text);
        answersField = view.findViewById(R.id.answers_field);

        questionIllustration.setImageDrawable(getResources().getDrawable(R.drawable.test_background));
        questionText.setText(mTest.getQuestion());

        createQuestionButtons(answersField);
        mCustomViewPager.setPagingEnabled(false);
        return view;
    }

    private void createQuestionButtons(LinearLayout answersField){
        int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        int gravity = Gravity.CENTER_HORIZONTAL;

        //конвертируем dp в пиксели
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 4, getResources().getDisplayMetrics());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(matchParent,wrapContent);
        params.gravity = gravity;
        params.setMargins(0,0,0,value);
        Map<String,Boolean> answers = mTest.getAnswers();

        //Создаем кнопки с вариантами ответа
        for (final Map.Entry<String,Boolean> pair : answers.entrySet()){
            Button button = new Button(getActivity());
            button.setText(pair.getKey());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //По нажатию проверяем ответ на корректность, меняем страницу на следующий вопрос
                    String isCorrect = getResources().getString(R.string.incorrect);
                    int color = R.color.colorOrange;
                    if (pair.getValue()){
                        mLesson.setCorrectAnswersCount(mLesson.getCorrectAnswersCount() + 1);
                        isCorrect = getResources().getString(R.string.correct);
                        color = R.color.colorNeonGreen;
                    }

                    Snackbar snackbar = Snackbar.make(view,isCorrect,Snackbar.LENGTH_SHORT);
                    View snackView = snackbar.getView();
                    snackView.setBackgroundColor(ContextCompat.getColor(getActivity(), color));
                    snackbar.show();

                    mCustomViewPager.setPagingEnabled(true);
                    mCustomViewPager.nextPage();
                }
            });

            answersField.addView(button,params);
        }
    }
}
