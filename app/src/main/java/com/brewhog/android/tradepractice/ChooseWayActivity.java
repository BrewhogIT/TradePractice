package com.brewhog.android.tradepractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseWayActivity extends AppCompatActivity {
    private Button okButton;
    private Button cancelButton;
    private ImageView chooseWayImage;
    private TextView failTextView;
    private CardView activityWindow;

    public static final String EXTRA_WINDOW_KIND = "com.brewhog.android.tradepractice.window_kind";
    public static final String EXTRA_PERCENT_OF_CORRECT = "com.brewhog.android.tradepractice.percent_of_correct_answers";
    public static final int START_NEW_TEST = 0;
    public static final int RESTART_TEST = 1;
    public static final int TEST_DONE = 2;
    private int windowKind;
    private int correctAnswersPercent;

    public static Intent newIntent(Context context,int windowKind,int percent){
        Intent intent = new Intent(context,ChooseWayActivity.class);
        intent.putExtra(EXTRA_WINDOW_KIND,windowKind);
        intent.putExtra(EXTRA_PERCENT_OF_CORRECT,percent);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_way);

        okButton = findViewById(R.id.ok_button);
        cancelButton = findViewById(R.id.cancel_button);
        chooseWayImage = findViewById(R.id.choose_window_image);
        failTextView = findViewById(R.id.fail_text_view);
        activityWindow = findViewById(R.id.choose_way_window);

        //включаем анимацию кругового открытия активности
        if (savedInstanceState == null){
            activityWindow.setVisibility(View.INVISIBLE);

            ViewTreeObserver viewTreeObserver = activityWindow.getViewTreeObserver();

            if (viewTreeObserver.isAlive()){
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        activityWindow.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }

        }

        Drawable image = null;
        windowKind = getIntent().getIntExtra(EXTRA_WINDOW_KIND,0);
        correctAnswersPercent = getIntent().getIntExtra(EXTRA_PERCENT_OF_CORRECT,0);
        String failText = getResources().getString(R.string.fail_text, correctAnswersPercent);

        switch (windowKind) {
            case START_NEW_TEST:
                image = getResources().getDrawable(R.drawable.start_test);

                okButton.setText(R.string.start_test);
                cancelButton.setText(R.string.return_to_lesson);
                break;
            case RESTART_TEST:
                image = getResources().getDrawable(R.drawable.fail_test);

                failTextView.setVisibility(View.VISIBLE);
                failTextView.setText(failText);

                okButton.setText(R.string.try_again);
                cancelButton.setText(R.string.return_to_lesson);
                break;
            case TEST_DONE:
                image = getResources().getDrawable(R.drawable.pass_test);

                cancelButton.setVisibility(View.GONE);
                okButton.setText(R.string.test_passed);

                break;
        }
        chooseWayImage.setImageDrawable(image);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeWithResult(Activity.RESULT_OK);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeWithResult(Activity.RESULT_CANCELED);
            }
        });
    }

    private void circularRevealActivity() {
        overridePendingTransition(0, 0);

        int centerX = activityWindow.getWidth() / 2;
        int centerY = activityWindow.getHeight() / 2;

        float finalRadius = Math.max(activityWindow.getWidth(),activityWindow.getHeight());

        Animator circularAnimation = ViewAnimationUtils.createCircularReveal(
                activityWindow,
                centerX,
                centerY,
                0,
                finalRadius);

        circularAnimation.setDuration(750);
        activityWindow.setVisibility(View.VISIBLE);
        circularAnimation.start();
    }

    private void closeWithAnimation(){
        int centerX = activityWindow.getWidth() / 2;
        int centerY = activityWindow.getHeight() / 2;

        float startRadius = Math.max(activityWindow.getWidth(),activityWindow.getHeight());

        Animator circularAnimation = ViewAnimationUtils.createCircularReveal(
                activityWindow,
                centerX,
                centerY,
                startRadius,
                0);

        circularAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                activityWindow.setVisibility(View.INVISIBLE);
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        circularAnimation.setDuration(750);
        circularAnimation.start();
    }

    @Override
    public void onBackPressed() {
        closeWithResult(Activity.RESULT_CANCELED);
    }

    private void closeWithResult(int resultCanceled) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_WINDOW_KIND, windowKind);
        setResult(resultCanceled, intent);
        closeWithAnimation();
    }
}
