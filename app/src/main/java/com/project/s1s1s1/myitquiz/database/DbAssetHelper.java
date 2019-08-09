package com.project.s1s1s1.myitquiz.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.LinearLayout;

import com.project.s1s1s1.myitquiz.dataModel.Quiz;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DbAssetHelper extends SQLiteAssetHelper {

    private static final String TAG = DbAssetHelper.class.getSimpleName();

    private String Table_Name;
    private static final int DB_VERSION=1;
    private static final String ID = "_id";//name of column1
    private static final String QUESTION = "Question";//name of column2
    private static final String OptionA = "OptionA";//name of column3
    private static final String OptionB = "OptionB";//name of column4
    private static final String OptionC = "OptionC";//name of column5
    private static final String OptionD = "OptionD";//name of column6
    private static final String ANSWER = "Answer";//name of column7
    private  SQLiteDatabase sqliteDb;


    public DbAssetHelper(Context context, String Database_Name, String Table_Name) {
        super(context, Database_Name,null, DB_VERSION);
        this.Table_Name=Table_Name;
        sqliteDb=getReadableDatabase();
    }


    public int rowCount() {
        int item=0;
        try {
            if (sqliteDb!=null) {
                Cursor cursor = sqliteDb.rawQuery("SELECT * FROM " + Table_Name, null); // getting size of the table
                item = cursor.getCount();
                cursor.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return item;
    }

    public List<Quiz> getAllQuestions() {
        List<Quiz> quizList = new ArrayList<>();
        try{
            if (sqliteDb!=null){
                String sql = "SELECT * FROM " + Table_Name;
                Cursor cursor = sqliteDb.rawQuery(sql, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        Quiz quiz = new Quiz();
                        quiz.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        quiz.setQuestion(cursor.getString(cursor.getColumnIndex(QUESTION)));
                        quiz.setOptionA(cursor.getString(cursor.getColumnIndex(OptionA)));
                        quiz.setOptionB(cursor.getString(cursor.getColumnIndex(OptionB)));
                        quiz.setOptionC(cursor.getString(cursor.getColumnIndex(OptionC)));
                        quiz.setOptionD(cursor.getString(cursor.getColumnIndex(OptionD)));
                        quiz.setAnswer(cursor.getString(cursor.getColumnIndex(ANSWER)));
                        quizList.add(quiz);
                    } while (cursor.moveToNext());
                    cursor.close();
                }
            }else {
                Log.e(TAG, "db is null " );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Collections.shuffle(quizList);
        return  quizList;
    }
}
