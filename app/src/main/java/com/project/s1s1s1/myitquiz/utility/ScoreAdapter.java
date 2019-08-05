package com.project.s1s1s1.myitquiz.utility;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.activity.ScoreCardActivity;

import java.util.HashMap;
import java.util.Set;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.MyViewHolder> {

    private String[] subjects;
    private Integer[] values;
    private HashMap score;

    public ScoreAdapter(HashMap score) {
        this.score = score;
        Set<String> subjectKey = score.keySet();   // extracting key of hashmap
        subjects = subjectKey.toArray(new String[subjectKey.size()]);     // extracting key of hashmap
        values = (Integer[]) score.values().toArray(new Integer[score.size()]);     // extracting values of hashmap
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String str = subjects[position];
        int number = values[position];
        holder.subTV.setText(str);
        holder.scoreTV.setText(String.valueOf(number));
    }

    @Override
    public int getItemCount() {
        return score.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView subTV, scoreTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subTV = itemView.findViewById(R.id.subTV);
            scoreTV = itemView.findViewById(R.id.scoreTV);
        }
    }
}