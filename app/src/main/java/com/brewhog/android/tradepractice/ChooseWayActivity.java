package com.brewhog.android.tradepractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.UUID;

public class ChooseWayActivity extends AppCompatActivity {
    private Button okButton;
    private Button cancelButton;
    private ImageView chooseWayImage;
    private TextView failTextView;

    public static final String EXTRA_WINDOW_KIND = "com.brewhog.android.tradepractice.window_kind";
    public static final String EXTRA_PERCENT_OF_CORRECT = "com.brewhog.android.tradepractice.percent_of_correct_answers";
    public static final int START_NEW_TEST = 0;
    public static final int RESTART_TEST = 1;
    public static final int TEST_DONE = 2;
    private int windowKind;
    private int correctAncwersPercent;

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

        Drawable image = null;
        windowKind = getIntent().getIntExtra(EXTRA_WINDOW_KIND,0);
        correctAncwersPercent = getIntent().getIntExtra(EXTRA_PERCENT_OF_CORRECT,0);
        String failText = getResources().getString(R.string.fail_text,correctAncwersPercent);

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
                Intent intent = new Intent();
                intent.putExtra(EXTRA_WINDOW_KIND,windowKind);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_WINDOW_KIND,windowKind);
                setResult(Activity.RESULT_CANCELED,intent);
                finish();
            }
        });
    }
}
