package com.project.s1s1s1.myitquiz.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.dataModel.User;
import com.project.s1s1s1.myitquiz.utility.UserPreference;
import com.project.s1s1s1.myitquiz.utility.ScoreAdapter;
import com.project.s1s1s1.myitquiz.utility.VolleyResponse;


import static com.project.s1s1s1.myitquiz.dataModel.User.getScore;
import static com.project.s1s1s1.myitquiz.utility.VolleyResponse.getUserDetailFromServer;

public class ScoreCardActivity extends AppCompatActivity implements VolleyResponse.NewObj {

    private final String TAG = ScoreCardActivity.class.getSimpleName();
    private ScoreAdapter adapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserPreference object = new UserPreference(this);
        user = object.getUserData();
        getUserDetailFromServer(this, user);

        setContentView(R.layout.activity_score_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundResource(R.drawable.mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView scoreRV = findViewById(R.id.scoreRV);
        scoreRV.setHasFixedSize(true);
        scoreRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        adapter = new ScoreAdapter(getScore(user));
        scoreRV.setAdapter(adapter);

    }

    @Override
    public void newProfileUser(User user) {
        this.user = user;
        adapter.notifyDataSetChanged();
    }

}




