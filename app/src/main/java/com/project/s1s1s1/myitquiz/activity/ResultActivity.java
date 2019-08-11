package com.project.s1s1s1.myitquiz.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.dataModel.User;
import com.project.s1s1s1.myitquiz.utility.Constant;
import com.project.s1s1s1.myitquiz.utility.UserPreference;
import com.project.s1s1s1.myitquiz.utility.VolleyResponse;

import static com.project.s1s1s1.myitquiz.utility.Utils.isNetworkAvailable;
import static com.project.s1s1s1.myitquiz.utility.VolleyResponse.updateUser;

public class ResultActivity extends AppCompatActivity implements VolleyResponse.NewObj {

    TextView correctTV, incorrectTV, attemptedTV, scoreTV, commentTV;
    ImageView imageView1, imageView2, imageView3, imageView4;
    int correct_ans = 0, incorr_ans = 0, attempt = 0, score = 0;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        UserPreference object = new UserPreference(this);

        user = object.getUserData();
        if (isNetworkAvailable(this)) {
            user.setSync_status(Constant.SYNC_STATUS_OK);
            updateUser(this, user);
        } else {
            user.setSync_status(Constant.SYNC_STATUS_FAILED);
            object.saveUserData(user);
        }

        correct_ans = user.getUserScore().getCorrectAns();
        attempt = user.getUserScore().getQuestionFaced();
        incorr_ans = user.getUserScore().getWrongAns();

        imageView1 = findViewById(R.id.five__stars);
        imageView2 = findViewById(R.id.five_likes);
        imageView3 = findViewById(R.id.five_dislike);
        imageView4 = findViewById(R.id.five_eggs);


        score = 10 * correct_ans;
        correctTV = findViewById(R.id.correct);
        incorrectTV = findViewById(R.id.incorrect);
        attemptedTV = findViewById(R.id.attempted);
        scoreTV = findViewById(R.id.score);
        commentTV = findViewById(R.id.you);


        attemptedTV.setText("  " + attempt);
        correctTV.setText("  " + correct_ans);
        incorrectTV.setText("  " + incorr_ans);
        scoreTV.setText("Score  :    " + score);
        if (correct_ans > 0) {
            float x1 = (correct_ans * 100) / attempt;

            if (x1 >= 75) {
                commentTV.setText(R.string.cmnt_excellent);
                imageView1.setVisibility(View.VISIBLE);
            } else if (x1 >= 50 && x1 < 75) {
                commentTV.setText(R.string.cmnt_avg);
                imageView2.setVisibility(View.VISIBLE);
            } else if (x1 >= 25 && x1 < 50) {
                commentTV.setText(R.string.cmnt_below_avg);
                imageView3.setVisibility(View.VISIBLE);
            } else {
                commentTV.setText(R.string.cmnt_loser);
                imageView4.setVisibility(View.VISIBLE);
            }
        } else {
            commentTV.setText(R.string.cmnt_loser);
            imageView4.setVisibility(View.VISIBLE);
        }

        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        };
        th.start();

    }

    @Override
    public void onBackPressed() {
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void newProfileUser(User user) {
        this.user = user;
    }
}
