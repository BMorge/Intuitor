package com.bmorge.Intuitor;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;

/**
 * Created by Dexp on 23.09.2016.
 */
public class AboutGameActivity extends Activity {
    MediaPlayer mediaPlayer;
    TextView textBestResult;
    TextView countBestResult;
    TextView textGamePlayed;
    TextView countGamePlayed;
    TextView textVersionGame;
    TextView countVersionGame;
    TextView textBankSize;
    TextView countBankSize;
    TextView textNameProgramer;
    TextView textProgramer;
    TextView textNameContent;
    TextView textContent;
    TextView textNamePainter;
    TextView textPainter;
    TextView textAnotherSite;
    static boolean flagmusic = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutgame);


        textBestResult = (TextView) findViewById(R.id.textBestResult);
        countBestResult = (TextView) findViewById(R.id.countBestResult);
        textGamePlayed = (TextView) findViewById(R.id.textGamePlayed);
        countGamePlayed = (TextView) findViewById(R.id.countGamePlayed);
        textVersionGame = (TextView) findViewById(R.id.textVersionGame);
        countVersionGame = (TextView) findViewById(R.id.countVersionGame);
        textBankSize = (TextView) findViewById(R.id.textBankSize);
        countBankSize = (TextView) findViewById(R.id.countBankSize);
        textNameProgramer = (TextView) findViewById(R.id.textNameProgramer);
        textProgramer = (TextView) findViewById(R.id.textProgramer);
        textNameContent = (TextView) findViewById(R.id.textNameContent);
        textContent = (TextView) findViewById(R.id.textContent);
        textNamePainter = (TextView) findViewById(R.id.textNamePainter);
        textPainter = (TextView) findViewById(R.id.textPainter);
        textAnotherSite = (TextView) findViewById(R.id.textAnotherSite);

        Typeface font_style = Typeface.createFromAsset(getAssets(), "9967.ttf");
        textBestResult.setTypeface(font_style);
        countBestResult.setTypeface(font_style);
        textGamePlayed.setTypeface(font_style);
        countGamePlayed.setTypeface(font_style);
        textVersionGame.setTypeface(font_style);
        countVersionGame.setTypeface(font_style);
        textBankSize.setTypeface(font_style);
        countBankSize.setTypeface(font_style);
        textNameProgramer.setTypeface(font_style);
        textProgramer.setTypeface(font_style);
        textNameContent.setTypeface(font_style);
        textContent.setTypeface(font_style);
        textNamePainter.setTypeface(font_style);
        textPainter.setTypeface(font_style);
        textAnotherSite.setTypeface(font_style);

        countBestResult.setText(""+ Player.getInstance().topGameCount);
        countGamePlayed.setText(""+ Player.getInstance().gameCount);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        if(metrics.heightPixels >= 1100){
            textBestResult.setTextSize(40);
            countBestResult.setTextSize(40);
            textGamePlayed.setTextSize(40);
            countGamePlayed.setTextSize(40);
            textVersionGame.setTextSize(40);
            countVersionGame.setTextSize(40);
            textBankSize.setTextSize(40);
            countBankSize.setTextSize(40);
            textNameProgramer.setTextSize(40);
            textProgramer.setTextSize(40);
            textNameContent.setTextSize(40);
            textContent.setTextSize(40);
            textNamePainter.setTextSize(40);
            textPainter.setTextSize(40);
            textAnotherSite.setTextSize(25);
            textProgramer.setPadding(0,0,80,0);
        }
        if (metrics.heightPixels >= 1500){
            textBestResult.setTextSize(50);
            countBestResult.setTextSize(50);
            textGamePlayed.setTextSize(50);
            countGamePlayed.setTextSize(50);
            textVersionGame.setTextSize(50);
            countVersionGame.setTextSize(50);
            textBankSize.setTextSize(50);
            countBankSize.setTextSize(50);
            textNameProgramer.setTextSize(50);
            textProgramer.setTextSize(50);
            textNameContent.setTextSize(50);
            textContent.setTextSize(50);
            textNamePainter.setTextSize(50);
            textPainter.setTextSize(50);
            textAnotherSite.setTextSize(30);
        }



    }


    @Override
    protected void onRestart() {

        Intent i = new Intent(this, MusicService.class);
        if (Player.getInstance().music)this.startService(i);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Intent i = new Intent(this, MusicService.class);
        if(!flagmusic)this.stopService(i);
        super.onStop();
    }



    @Override
    public void finish() {
        flagmusic = true;
        super.finish();
        overridePendingTransition(R.anim.alfa0sun,R.anim.for0faid0about);
    }





}
