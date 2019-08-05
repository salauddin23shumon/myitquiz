package com.project.s1s1s1.myitquiz.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.s1s1s1.myitquiz.activity.PlayGameActivity;
import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.dataModel.QuizMenu;

import java.util.List;

public class QuizViewAdapter extends RecyclerView.Adapter<QuizViewAdapter.MyViewHolder> {

    private Context context;
    private List<QuizMenu> quizMenuList;
    public final static String Message = "com.project.s1s1s1.myitquiz.MESSAGE";

    public QuizViewAdapter(Context context, List<QuizMenu> quizMenuList) {
        this.context = context;
        this.quizMenuList = quizMenuList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final QuizMenu singleQuiz = quizMenuList.get(position);
        final String subject = singleQuiz.getSubject();
        holder.itemIV.setImageResource(singleQuiz.getThumbnail());
        holder.itemIV.setAlpha(200);    // transparency
        holder.quizCV.setCardBackgroundColor(R.drawable.rainbow_bg);
        holder.itemIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, ""+singleQuiz.getId(), Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, PlayGameActivity.class).putExtra(Message, subject));
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return quizMenuList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView itemIV;
        private CardView quizCV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemIV = itemView.findViewById(R.id.itemIV);
            quizCV = itemView.findViewById(R.id.quizCV);
        }
    }
}
