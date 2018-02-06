package com.bmorge.Intuitor;

import java.io.Serializable;

/**
 * Created by Dexp on 23.09.2016.
 */
public class Question implements Serializable{
    public String questionText;
    public String urlImg1true;
    public String urlImg2;
    public String urlImg3;
    public String urlImg4;
    public String afterText;
    public String questionTag;

    public Question(String questionText, String urlImg1true, String urlImg2, String urlImg3, String urlImg4, String afterText, String questionTag) {
        this.questionText = questionText;
        this.urlImg1true = urlImg1true;
        this.urlImg2 = urlImg2;
        this.urlImg3 = urlImg3;
        this.urlImg4 = urlImg4;
        this.afterText = afterText;
        this.questionTag = questionTag;
    }
    public Question(){}

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getUrlImg1true() {
        return urlImg1true;
    }

    public void setUrlImg1true(String urlImg1true) {
        this.urlImg1true = urlImg1true;
    }

    public String getUrlImg2() {
        return urlImg2;
    }

    public void setUrlImg2(String urlImg2) {
        this.urlImg2 = urlImg2;
    }

    public String getUrlImg3() {
        return urlImg3;
    }

    public void setUrlImg3(String urlImg3) {
        this.urlImg3 = urlImg3;
    }

    public String getUrlImg4() {
        return urlImg4;
    }

    public void setUrlImg4(String urlImg4) {
        this.urlImg4 = urlImg4;
    }

    public String getAfterText() {
        return afterText;
    }

    public void setAfterText(String afterText) {
        this.afterText = afterText;
    }

    public String getQuestionTag() {
        return questionTag;
    }

    public void setQuestionTag(String questionTag) {
        this.questionTag = questionTag;
    }
}
