package com.project.s1s1s1.myitquiz.dataModel;

import com.project.s1s1s1.myitquiz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizMenu {

    private int id;
    private int thumbnail;
    private String subject;


    public QuizMenu(int id, int thumbnail, String subject) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public static List<QuizMenu> getAllQuiz() {
        List<QuizMenu> quizMenuList = new ArrayList<>();
        quizMenuList.add(new QuizMenu(1, R.drawable.btn1,"computer"));
        quizMenuList.add(new QuizMenu(2, R.drawable.btn2,"mysql"));
        quizMenuList.add(new QuizMenu(3, R.drawable.btn3,"php"));
        quizMenuList.add(new QuizMenu(4, R.drawable.btn4,"c_programming"));
        quizMenuList.add(new QuizMenu(5, R.drawable.btn5,"html"));
        quizMenuList.add(new QuizMenu(6, R.drawable.btn6,"css"));
        quizMenuList.add(new QuizMenu(7, R.drawable.btn7,"java"));
        quizMenuList.add(new QuizMenu(8, R.drawable.btn8,"c#"));
        quizMenuList.add(new QuizMenu(9, R.drawable.btn9,"javascript"));
        quizMenuList.add(new QuizMenu(10, R.drawable.btn10,"data_structure"));
        quizMenuList.add(new QuizMenu(11, R.drawable.btn11,"c++"));
        quizMenuList.add(new QuizMenu(12, R.drawable.btn12,"digital_logic"));
        Collections.shuffle(quizMenuList);
        return quizMenuList;
    }
}
