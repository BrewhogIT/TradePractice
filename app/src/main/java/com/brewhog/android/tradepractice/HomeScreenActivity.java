package com.brewhog.android.tradepractice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {
    private ImageView logoView;
    private ViewPager lessonSectionPager;
    private List<Integer> lessonKind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        logoView = findViewById(R.id.logo_view);
        Drawable logo = getResources().getDrawable(R.drawable.logo);
        logoView.setImageDrawable(logo);

        lessonKind = new ArrayList<>();
        lessonKind.add(R.string.theory);
        lessonKind.add(R.string.practice);
        lessonKind.add(R.string.how_to_use);

        FragmentManager fragmentManager = getSupportFragmentManager();
        lessonSectionPager = findViewById(R.id.home_screen_pager);
        lessonSectionPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return LessonSectionFragment.newInstance(lessonKind.get(position));
            }

            @Override
            public int getCount() {
                return lessonKind.size();
            }
        });

    }
}
