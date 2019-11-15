package com.brewhog.android.tradepractice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TheoryListFragment extends Fragment {
    RecyclerView theoryListRecyclerView;
    LessonPack mLessonPack;

    public static TheoryListFragment newInstance() {
        TheoryListFragment fragment = new TheoryListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theory_list,container,false);
        mLessonPack = LessonPack.getLessonPack(getActivity());

        theoryListRecyclerView = view.findViewById(R.id.theory_recycler_view);
        theoryListRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        TheoryAdapter adapter = new TheoryAdapter(mLessonPack.getLessons());
        theoryListRecyclerView.setAdapter(adapter);

        return view;
    }

    private class TheoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView lessonPreview;
        private TextView lessonTopic;

        public TheoryHolder(@NonNull View itemView) {
            super(itemView);
            lessonPreview = itemView.findViewById(R.id.lesson_preview);
            lessonTopic = itemView.findViewById(R.id.lesson_topic);
            itemView.setOnClickListener(this);
        }

        public void bind(Lesson lesson){
            lessonPreview.setImageDrawable(lesson.getPreview());
            lessonTopic.setText(lesson.getTopic());
        }

        @Override
        public void onClick(View view) {
            //Open Lesson
        }
    }

    private class TheoryAdapter extends RecyclerView.Adapter<TheoryHolder>{

        List<Lesson> mLessons;

        public TheoryAdapter(List<Lesson> lessons) {
            mLessons = lessons;
        }

        @NonNull
        @Override
        public TheoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_theory, parent, false);
            TheoryHolder holder = new TheoryHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull TheoryHolder holder, int position) {
            Lesson lesson = mLessons.get(position);
            holder.bind(lesson);
        }

        @Override
        public int getItemCount() {
            return mLessons.size();
        }
    }
}
