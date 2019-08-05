package com.project.s1s1s1.myitquiz.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.material.snackbar.Snackbar;
import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.database.DbAssetHelper;
import com.project.s1s1s1.myitquiz.dataModel.Quiz;
import com.project.s1s1s1.myitquiz.dataModel.Score;
import com.project.s1s1s1.myitquiz.dataModel.User;
import com.project.s1s1s1.myitquiz.splashScreen.GameOverSplash;
import com.project.s1s1s1.myitquiz.splashScreen.LevelCompletedSplash;
import com.project.s1s1s1.myitquiz.utility.PreferenceObject;
import com.project.s1s1s1.myitquiz.utility.QuizViewAdapter;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;
import static com.project.s1s1s1.myitquiz.utility.Utils.setBtnAnim;


public class PlayGameActivity extends AppCompatActivity {

    private static final String TAG = PlayGameActivity.class.getSimpleName();
    private DonutProgress donutProgress;
    private long countDownInterval = 1000, remainingMillis, millisInFuture;
    private CountDownTimer countDownTimer;
    private TextView questionTV;
    private Button btn_optA, btn_optB, btn_optC, btn_optD, btnPlay;
    private String subject, answer = null, optionA, optionB, optionC, optionD;
    private DbAssetHelper helper;
    private User user;
    private int visibility = 0, timer = 101, numberOfQuestion = 0, correct_ans = 0, displayQuiz = 1, soundValue;
    private List<Quiz> quizList = new ArrayList<>();
    MediaPlayer mediaPlayer, start_sound, wrong_beep, right_beep;
    GifImageView gifImageView;
    Handler gameHandler;
    Runnable delayRunnable;
    Dialog alertDialog;
    private PreferenceObject preferenceObject;
    private boolean isGameActive;
    TelephonyManager mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        gameHandler = new Handler();
        preferenceObject = new PreferenceObject(this);
        user = preferenceObject.getUserData();

        subject = getIntent().getStringExtra(QuizViewAdapter.Message);
        createDb();

        gifImageView = findViewById(R.id.gifview);
        gifImageView.setVisibility(View.VISIBLE);

        donutProgress = findViewById(R.id.donut_progress);
        donutProgress.setMax(100);
        donutProgress.setFinishedStrokeColor(Color.parseColor("#FF04C4FF"));
        donutProgress.setTextColor(Color.parseColor("#FF43D205"));
        donutProgress.setKeepScreenOn(true);

        SharedPreferences sharedPreferences_sound = getSharedPreferences("Sound_Pref", Context.MODE_PRIVATE);
        soundValue = sharedPreferences_sound.getInt("Sound", 0);

        start_sound = MediaPlayer.create(this, R.raw.start23);
        wrong_beep = MediaPlayer.create(this, R.raw.error_beep);
        right_beep = MediaPlayer.create(this, R.raw.point_beep);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound_fx2);

        btn_optA = findViewById(R.id.btn_optA);
        btn_optB = findViewById(R.id.btn_optB);
        btn_optC = findViewById(R.id.btn_optC);
        btn_optD = findViewById(R.id.btn_optD);
        questionTV = findViewById(R.id.questionTV);
        btnPlay = findViewById(R.id.btn_play);
        btnPlay.setAnimation(setBtnAnim());


        mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private void createDb() {

        if (subject.equals("computer")) {
            helper = new DbAssetHelper(this, "computer.db", "computer");
        } else if (subject.equals("mysql")) {
            helper = new DbAssetHelper(this, "mysql.db", "mysql");
        } else if (subject.equals("php")) {
            helper = new DbAssetHelper(this, "php.db", "php");
        } else if (subject.equals("c_programming")) {
            helper = new DbAssetHelper(this, "c_language.db", "c_language");
        } else if (subject.equals("html")) {
            helper = new DbAssetHelper(this, "html.db", "html");
        } else if (subject.equals("css")) {
            helper = new DbAssetHelper(this, "css.db", "css");
        } else if (subject.equals("java")) {
            helper = new DbAssetHelper(this, "java.db", "java");
        } else if (subject.equals("c#")) {
            helper = new DbAssetHelper(this, "csharp.db", "csharp");
        } else if (subject.equals("javascript")) {
            helper = new DbAssetHelper(this, "js.db", "js");
        } else if (subject.equals("data_structure")) {
            helper = new DbAssetHelper(this, "data_structure.db", "dataStructure");
        } else if (subject.equals("c++")) {
            helper = new DbAssetHelper(this, "cpp.db", "cpp");
        } else if (subject.equals("digital_logic")) {
            helper = new DbAssetHelper(this, "dld.db", "dld");
        }
        quizList = helper.getAllQuestions();
        Log.e(TAG, "onCreate: " + quizList.size());
    }

    public void executeQuiz(View view) {
        numberOfQuestion++;
        if (answer != null) {
            setViewResult(view);
        }
        makeDbReadable(helper);
    }

    private void setTimer(final long millisInFuture, final long countDownInterval, int delay) {

        gameHandler.postDelayed(delayRunnable = new Runnable() {
            public void run() {
                Log.d(TAG, "run: snd" + soundValue);
                if (soundValue == 0) {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }

                isGameActive = true;
                btn_optA.setVisibility(View.VISIBLE);
                btn_optB.setVisibility(View.VISIBLE);
                btn_optC.setVisibility(View.VISIBLE);
                btn_optD.setVisibility(View.VISIBLE);
                questionTV.setVisibility(View.VISIBLE);
                gifImageView.setVisibility(View.GONE);
                donutProgress.setVisibility(View.VISIBLE);
                visibility = 1;

                countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        timer = timer - 1;
                        donutProgress.setProgress(timer);
                        remainingMillis = millisUntilFinished;
                        Log.e(TAG, "onTick: " + remainingMillis);
                    }

                    @Override
                    public void onFinish() {
                        donutProgress.setProgress(0);
                        openNxtActivity(GameOverSplash.class);
                        isGameActive = false;
                    }
                }.start();
            }
        }, delay * 1000);   /////////delay time/////////
    }

    private void setScore() {
        int points = correct_ans * 10;
        Score score = user.getUserScore();
        switch (subject) {
            case "computer":
                if (user.getUserScore().getComputer() < points)
                    score.setComputer(points);
                break;
            case "mysql":
                if (user.getUserScore().getMySql() < points)
                    score.setMySql(points);
                break;
            case "php":
                if (user.getUserScore().getPhp() < points)
                    score.setPhp(points);
                break;
            case "c_programming":
                if (user.getUserScore().getcProgramming() < points)
                    score.setcProgramming(points);
                break;
            case "html":
                if (user.getUserScore().getHtml() < points)
                    score.setHtml(points);
                break;
            case "css":
                if (user.getUserScore().getCss() < points)
                    score.setCss(points);
                break;
            case "java":
                if (user.getUserScore().getJava() < points)
                    score.setJava(points);
                break;
            case "c#":
                if (user.getUserScore().getcSharp() < points)
                    score.setcSharp(points);
                break;
            case "javascript":
                if (user.getUserScore().getJavaScript() < points)
                    score.setJavaScript(points);
                break;
            case "data_structure":
                if (user.getUserScore().getDataStructure() < points)
                    score.setDataStructure(points);
                break;
            case "c++":
                if (user.getUserScore().getCpp() < points)
                    score.setCpp(points);
                break;
            case "digital_logic":
                if (user.getUserScore().getDigitalLogic() < points)
                    score.setDigitalLogic(points);
                break;
            default:
                System.out.println("no match");
        }
        score.setQuestionFaced(numberOfQuestion);
        score.setCorrectAns(correct_ans);
        score.setWrongAns(numberOfQuestion - correct_ans);
        user.setUserScore(score);
        preferenceObject.saveUserData(user);
    }

    private void setViewResult(View view) {
        switch (answer) {
            case "A":
                if (view.getId() == R.id.btn_optA) {
                    onRightAnswerSelected(view);
                } else {
                    onWrongAnswerSelected(view, optionA);
                }
                break;
            case "B":
                if (view.getId() == R.id.btn_optB) {
                    onRightAnswerSelected(view);
                } else {
                    onWrongAnswerSelected(view, optionB);
                }
                break;
            case "C":
                if (view.getId() == R.id.btn_optC) {
                    onRightAnswerSelected(view);
                } else {
                    onWrongAnswerSelected(view, optionC);
                }
                break;
            case "D":
                if (view.getId() == R.id.btn_optD) {
                    onRightAnswerSelected(view);
                } else {
                    onWrongAnswerSelected(view, optionD);
                }
                break;
        }
    }

    private void onWrongAnswerSelected(View view, String option) {
        Snackbar.make(view, "✖ Incorrect\t      Answer : " + option + "", Snackbar.LENGTH_SHORT).show();
        wrong_beep.start();
    }

    private void onRightAnswerSelected(View view) {
        Snackbar.make(view, "         Correct ☺", Snackbar.LENGTH_SHORT).show();
        right_beep.start();
        correct_ans++;
    }

    private void makeDbReadable(DbAssetHelper helper) {
        int rowCount = helper.rowCount();
        if (displayQuiz == rowCount) {
            openNxtActivity(LevelCompletedSplash.class);
            displayQuiz = 0;
        } else {
            readDb(quizList);
            displayQuiz++;
        }
    }

    private void readDb(List<Quiz> quizList) {
        Log.e(TAG, "readDb: " + displayQuiz);
        String question = quizList.get(displayQuiz).getQuestion();
        optionA = quizList.get(displayQuiz).getOptionA();
        optionB = quizList.get(displayQuiz).getOptionB();
        optionC = quizList.get(displayQuiz).getOptionC();
        optionD = quizList.get(displayQuiz).getOptionD();
        answer = quizList.get(displayQuiz).getAnswer();

        questionTV.setText(String.valueOf(question));
        btn_optA.setText("A. " + optionA);
        btn_optB.setText("B. " + optionB);
        btn_optC.setText("C. " + optionC);
        btn_optD.setText("D. " + optionD);
    }

    @Override
    protected void onPause() {
        if (countDownTimer != null) {       // when game is started .... pausing the countdown timer
            countDownTimer.cancel();
            if (soundValue == 0)
                mediaPlayer.pause();
        }
        gameHandler.removeCallbacks(delayRunnable);     //play btn is clicked but game is not started
        Log.d(TAG, "onPause: called");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: called");
        if (alertDialog != null && alertDialog.isShowing()) {       //avoiding multiple dialog
            return;
        } else if (btnPlay.getVisibility() == View.VISIBLE) {       // btnPlay is clicked but onPause called before the game is start
            Log.d(TAG, "else if: ");
            return;
        }
        else {
            showResumeDialog();
            Log.d(TAG, "else : ");
        }

    }

    private void showResumeDialog() {
        alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.resume_game_dialog);
        alertDialog.setCancelable(false);
        final Button resumeBtn = alertDialog.findViewById(R.id.btnResume);
        resumeBtn.startAnimation(setBtnAnim());
        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countDownTimer == null) {
                    Log.e(TAG, "showResumeDialog: if");
                    setTimer(millisInFuture, countDownInterval, 1);
                    alertDialog.dismiss();
                } else {
                    Log.e(TAG, "showResumeDialog: else");
                    setTimer(remainingMillis - 1, countDownInterval, 1);
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (isGameActive) {   //  when game is started
            countDownTimer.cancel();
            if (soundValue == 0)
                mediaPlayer.pause();
            showExitDialog();
        } else {
            startActivity(new Intent(this, QuizActivity.class));
            Log.e(TAG, "onBackPressed: else " + isGameActive);
            super.onBackPressed();
        }
    }


    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setCancelable(false);
        builder.setTitle("Are you sure want to exit from the quiz? ");

        builder.setNegativeButton(Html.fromHtml("<font color='#FA0707'>No</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setTimer(remainingMillis - 1, countDownInterval, 1);
                Log.e(TAG, "alertCancle: " + remainingMillis);
            }
        });
        builder.setPositiveButton(Html.fromHtml("<font color='#25880A'>Yes</font>"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(PlayGameActivity.this, QuizActivity.class));
            }
        });
        builder.show();
    }

    public void openNxtActivity(Class<? extends Activity> ActivityToBeOpen) {
        Intent intent = new Intent(PlayGameActivity.this, ActivityToBeOpen);
        setScore();
        startActivity(intent);
        finish();
    }

    public void btnPlayClicked(View view) {
        if (visibility == 0) {
            btnPlay.setVisibility(View.GONE);
            btnPlay.clearAnimation();
            executeQuiz(view);
            start_sound.start();
            millisInFuture = 100000;
//            millisInFuture = 20000;
            setTimer(millisInFuture, countDownInterval, 5);
        }
    }

    @Override
    protected void onDestroy() {
        helper.close();     //closing DB
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }

    PhoneStateListener phoneStateListener = new PhoneStateListener() {      // pausing the system when incoming call is coming
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                onPause();
                onRestart();
            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                Log.d(TAG, "onCallStateChanged: CALL_STATE_IDLE");
//                onRestart();
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                Log.d(TAG, "onCallStateChanged: CALL_STATE_OFFHOOK");
                onPause();
                onRestart();
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };

}
