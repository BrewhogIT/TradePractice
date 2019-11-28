package com.brewhog.android.tradepractice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.UUID;

public class ChooseWayActivity extends AppCompatActivity {
    private Button okButton;
    private Button cancelButton;
    private ImageView chooseWayImage;
    private static final String EXTRA_WINDOW_KIND = "com.brewhog.android.tradepractice.window_kind";
    public static final UUID START_NEW_TEST = UUID.randomUUID();
    public static final UUID RESTART_TEST = UUID.randomUUID();

    public static Intent newIntent(Context context,UUID windowKind){
        Intent intent = new Intent(context,ChooseWayActivity.class);
        intent.putExtra(EXTRA_WINDOW_KIND,windowKind);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_way);

        okButton = findViewById(R.id.ok_button);
        cancelButton = findViewById(R.id.cancel_button);
        chooseWayImage = findViewById(R.id.choose_window_image);

        Drawable image;
        UUID windowKind = (UUID) getIntent().getSerializableExtra(EXTRA_WINDOW_KIND);

        if (windowKind.equals(START_NEW_TEST)){
            image = getResources().getDrawable(R.drawable.start_test);

            okButton.setText(R.string.start_test);
            cancelButton.setText(R.string.return_to_lesson);
            chooseWayImage.setImageDrawable(image);
        } else if (windowKind.equals(RESTART_TEST)){
            image = getResources().getDrawable(R.drawable.fail_test);

            okButton.setText(R.string.try_again);
            cancelButton.setText(R.string.return_to_lesson);
            chooseWayImage.setImageDrawable(image);
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
}
