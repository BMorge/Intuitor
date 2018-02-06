package com.bmorge.Intuitor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dexp on 23.09.2016.
 */
public class Player implements Serializable {
    private static volatile Player instance;
    public  String userName = "userName";
    public  int gameCount = 0;
    public  int topGameCount = 0;
    public  int trueQuestion = 0;
    public  boolean music = true;
    public  boolean haveBonusLive = false;
    public  ArrayList<Question> questionsBank = new ArrayList<Question>();
    public  int actualIdQuestion =-1;
    public int uslessOne;
    public int uslessTwo;



    private Player() {
    }
    public static Player getInstance(){
        if (instance == null){
            synchronized (Player.class){
                if (instance == null)instance = new Player();
            }
        }
        return instance;
    }

    public static void setInstance(Player playN){
        instance =playN;
    }


}
