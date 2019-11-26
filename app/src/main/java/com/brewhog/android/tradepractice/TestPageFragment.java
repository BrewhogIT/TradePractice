package com.brewhog.android.tradepractice;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TestPageFragment extends Fragment {
    private static final String ARG_LESSON_ID = "lesson_id";
    private static final String ARG_TEST_NUMBER = "test_number";
    private Test mTest;
    private ImageView questionIllustration;
    private TextView questionText;
    private LinearLayout answersField;

    public static TestPageFragment newInstance(UUID lessonID, int testNumber) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LESSON_ID,lessonID);
        args.putSerializable(ARG_TEST_NUMBER,testNumber);

        TestPageFragment fragment = new TestPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UUID lessonID =(UUID) getArguments().getSerializable(ARG_LESSON_ID);
        int testNumber = getArguments().getInt(ARG_TEST_NUMBER);
        Lesson lesson = LessonPack.getLessonPack(getActivity()).getLesson(lessonID);
        mTest = lesson.getLessonTest().get(testNumber);

        View view = inflater.inflate(R.layout.fragment_test_page,container,false);
        questionIllustration = view.findViewById(R.id.question_image);
        questionText = view.findViewById(R.id.question_text);
        answersField = view.findViewById(R.id.answers_field);

        questionIllustration.setImageDrawable(getResources().getDrawable(R.drawable.test_background));
        questionText.setText(mTest.getQuestion());

        createQuestionButtons(answersField);
        return view;
    }

    private void createQuestionButtons(LinearLayout forInput){
        int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
        int matchParent = LinearLayout.LayoutParams.MATCH_PARENT;
        int gravity = Gravity.CENTER_HORIZONTAL;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(matchParent,wrapContent);
        params.gravity = gravity;
        Map<String,Boolean> answers = mTest.getAnswers();

        for (Map.Entry<String,Boolean> pair : answers.entrySet()){
            Button button = new Button(getActivity());
            button.setText(pair.getKey());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Проверить значение из мапы, если true увеличить
                    //количество правильных ответов урока
                    //разблокировать переход на вопрос
                    //всплывающее окно "Правильно" иначе "Непраильно"
                }
            });

            forInput.addView(button,params);
        }
    }
}
