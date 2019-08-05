package com.project.s1s1s1.myitquiz.dataModel;


import com.project.s1s1s1.myitquiz.utility.Constant;
import com.project.s1s1s1.myitquiz.utility.Utils;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {
    private String id;
    private String userName;
    private String email;
    private String password;
    private String photo;
    private int sync_status;
    private Score userScore;


    public User(String id, String userName, String email, String password, String photo, Score userScore) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.userScore = userScore;
        sync_status= Constant.SYNC_STATUS_OK;
    }

    public User() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Score getUserScore() {
        return userScore;
    }

    public void setUserScore(Score userScore) {
        this.userScore = userScore;
    }

    public int getSync_status() {
        return sync_status;
    }

    public void setSync_status(int sync_status) {
        this.sync_status = sync_status;
    }

    public static HashMap<String, Integer> getScore(User user) {
        HashMap<String, Integer> userScore = new HashMap<>();
//        Score score = new Score();

        userScore.put("Computer", user.getUserScore().getComputer());
        userScore.put("MySql", user.getUserScore().getMySql());
        userScore.put("PHP", user.getUserScore().getPhp());
        userScore.put("C_Programming", user.getUserScore().getcProgramming());
        userScore.put("C#", user.getUserScore().getcSharp());
        userScore.put("C++", user.getUserScore().getCpp());
        userScore.put("Data Structure", user.getUserScore().getDataStructure());
        userScore.put("Javascript", user.getUserScore().getJavaScript());
        userScore.put("HTML", user.getUserScore().getHtml());
        userScore.put("CSS", user.getUserScore().getCss());
        userScore.put("JAVA", user.getUserScore().getJava());
        userScore.put("Digital Logic", user.getUserScore().getDigitalLogic());
        return userScore;
    }
}
