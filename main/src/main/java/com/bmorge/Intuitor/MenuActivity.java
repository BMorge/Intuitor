package com.bmorge.Intuitor;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.*;

public class MenuActivity extends BaseActivity {
    /**
     * Called when the activity is first created.
     */
    SoundPool soundPool;
    ImageButton radioButton;
    Button buttonPlay;
    Button buttonAbout;
    TextView menuTopResult;
    TextView ololoZahpdiEsho;
//todo реклама видео следить + верси 16
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmenu);
        FileInputStream fileInputStream;
        //deleteFile("Save");
        try {
            fileInputStream = openFileInput("Save");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Player.setInstance((Player) objectInputStream.readObject());
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        fixGui();

        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 1);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool.load(this, R.raw.click, 2);
        soundPool.load(this, R.raw.wrong0sound, 2);
        Intent i = new Intent(this, MusicService.class);
        if (Player.getInstance().music) this.startService(i);
        if(Player.getInstance().topGameCount == getResources().getInteger(R.integer.countquestions)) ololoZahpdiEsho.setText(getResources().getString(R.string.ololoZahodiesho));
        menuTopResult.setText(getResources().getString(R.string.textBestResult) + " " + Player.getInstance().topGameCount);
    }

    @Override
    protected void onRestart() {
        isNeedSmf = true;

        Intent i = new Intent(this, MusicService.class);
        if (Player.getInstance().music && (!AboutGameActivity.flagmusic || !PlayActivity.flagmusic)) this.startService(i);

        if(Player.getInstance().topGameCount == getResources().getInteger(R.integer.countquestions)) ololoZahpdiEsho.setText(getResources().getString(R.string.ololoZahodiesho));
        menuTopResult.setText(getResources().getString(R.string.textBestResult) + " " + Player.getInstance().topGameCount);
        super.onRestart();
    }

    public void playscreen(View view) {
        if (Player.getInstance().music) soundPool.play(1, 0.7f, 0.7f, 0, 0, 1);
        Intent intent = new Intent(this, PlayActivity.class);
        isNeedSmf = false;
        PlayActivity.flagmusic =false;
        startActivity(intent);
        overridePendingTransition(R.anim.left0to0right,R.anim.alpha0faide);

    }


    public void aboutscreen(View view) {
        if (Player.getInstance().music) soundPool.play(1, 0.7f, 0.7f, 0, 0, 1);
        Intent intent = new Intent(this, AboutGameActivity.class);
        isNeedSmf = false;
        AboutGameActivity.flagmusic = false;
        startActivity(intent);
        overridePendingTransition(R.anim.right0to0left,R.anim.alpha0faide);
    }

    public void musicBtnClick(View view) {
        radioButton = (ImageButton) findViewById(R.id.volumebtn);
        Intent i = new Intent(this, MusicService.class);
        if (Player.getInstance().music) {
            Player.getInstance().music = false;

            this.stopService(i);
            radioButton.setBackground(getResources().getDrawable(R.drawable.soundfalse));
        } else {
            Player.getInstance().music = true;

            this.startService(i);
            radioButton.setBackground(getResources().getDrawable(R.drawable.soundtrue));
        }
    }


    private void fixGui() {
        buttonPlay = (Button) findViewById(R.id.btnplay);
        buttonAbout = (Button) findViewById(R.id.btnaboutg);
        radioButton = (ImageButton) findViewById(R.id.volumebtn);
        menuTopResult = (TextView) findViewById(R.id.menutopresult);
        ololoZahpdiEsho = (TextView) findViewById(R.id.ololoZahodiEsho);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        buttonPlay.setWidth(metrics.widthPixels / 3 + 60);
        //buttonPlay.setHeight((int)(metrics.heightPixels / 2.5f));
        buttonPlay.setHeight(metrics.heightPixels / 3);
        buttonAbout.setWidth(metrics.widthPixels / 3 + 60);
        buttonAbout.setHeight(metrics.heightPixels / 3);
        radioButton.getLayoutParams().height = metrics.heightPixels / 6;
        radioButton.getLayoutParams().width = metrics.heightPixels / 6;
        if (!Player.getInstance().music) radioButton.setBackground(getResources().getDrawable(R.drawable.soundfalse));

        Typeface font_style = Typeface.createFromAsset(getAssets(), "9967.ttf");
        buttonPlay.setTypeface(font_style);
        buttonAbout.setTypeface(font_style);
        menuTopResult.setTypeface(font_style);
        ololoZahpdiEsho.setTypeface(font_style);
        if (metrics.heightPixels >= 1100) {
            menuTopResult.setTextSize(45);
            ololoZahpdiEsho.setTextSize(35);
        }
        if (metrics.heightPixels >= 1500) {
            menuTopResult.setTextSize(70);
            ololoZahpdiEsho.setTextSize(60);
        }

    }

    @Override
    protected void onPause() {
        PlayActivity.flagmusic =false;
        AboutGameActivity.flagmusic =false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent i = new Intent(this, MusicService.class);
        this.stopService(i);
        //  releaseMP();
        super.onDestroy();
    }


}
