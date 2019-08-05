package com.project.s1s1s1.myitquiz.dataModel;

import java.util.HashMap;

public class Score {

    private int cpp;
    private int cSharp;
    private int cProgramming;
    private int java;
    private int html;
    private int dataStructure;
    private int digitalLogic;
    private int css;
    private int javaScript;
    private int computer;
    private int mySql;
    private int php;

    private int questionFaced;
    private int correctAns;
    private int wrongAns;

    public Score(int cpp, int cSharp, int cProgramming, int java, int html, int dataStructure, int digitalLogic, int css, int javaScript, int computer, int mySql, int php) {
        this.cpp = cpp;
        this.cSharp = cSharp;
        this.cProgramming = cProgramming;
        this.java = java;
        this.html = html;
        this.dataStructure = dataStructure;
        this.digitalLogic = digitalLogic;
        this.css = css;
        this.javaScript = javaScript;
        this.computer = computer;
        this.mySql = mySql;
        this.php = php;
        questionFaced = 0;
        correctAns = 0;
        wrongAns = 0;

    }

    public Score() {
    }

    public int getCpp() {
        return cpp;
    }

    public void setCpp(int cpp) {
        this.cpp = cpp;
    }

    public int getcSharp() {
        return cSharp;
    }

    public void setcSharp(int cSharp) {
        this.cSharp = cSharp;
    }

    public int getcProgramming() {
        return cProgramming;
    }

    public void setcProgramming(int cProgramming) {
        this.cProgramming = cProgramming;
    }

    public int getJava() {
        return java;
    }

    public void setJava(int java) {
        this.java = java;
    }

    public int getHtml() {
        return html;
    }

    public void setHtml(int html) {
        this.html = html;
    }

    public int getDataStructure() {
        return dataStructure;
    }

    public void setDataStructure(int dataStructure) {
        this.dataStructure = dataStructure;
    }

    public int getDigitalLogic() {
        return digitalLogic;
    }

    public void setDigitalLogic(int digitalLogic) {
        this.digitalLogic = digitalLogic;
    }

    public int getCss() {
        return css;
    }

    public void setCss(int css) {
        this.css = css;
    }

    public int getJavaScript() {
        return javaScript;
    }

    public void setJavaScript(int javaScript) {
        this.javaScript = javaScript;
    }

    public int getComputer() {
        return computer;
    }

    public void setComputer(int computer) {
        this.computer = computer;
    }

    public int getMySql() {
        return mySql;
    }

    public void setMySql(int mySql) {
        this.mySql = mySql;
    }

    public int getPhp() {
        return php;
    }

    public void setPhp(int php) {
        this.php = php;
    }

    public int getQuestionFaced() {
        return questionFaced;
    }

    public void setQuestionFaced(int questionFaced) {
        this.questionFaced = questionFaced;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }

    public int getWrongAns() {
        return wrongAns;
    }

    public void setWrongAns(int wrongAns) {
        this.wrongAns = wrongAns;
    }
}
