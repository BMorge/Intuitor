package com.bmorge.Intuitor;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.appodeal.ads.Appodeal;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Random;


/**
 * Created by Dexp on 23.09.2016.
 */

public class PlayActivity extends Activity {

    String truAnswerBtnName;
    RelativeLayout questionLayout;
    TextView textquestion;
    ImageButton okbtnfortext;
    ImageButton imageOne;
    ImageButton imageTwo;
    ImageButton imageThree;
    ImageButton imageFour;
    ImageButton okbtn;
    ImageButton cancelbtn;
    ImageButton bonusLifeBtn;
    ImageView lifeOne;
    TextView countView;
    TextView repeatQ;
    boolean ugadal = false;
    boolean needRestart = false;
    int idQ;
    SoundPool soundPool;
    Random random = new Random();

    public static boolean isAppWentToBg = false;
    public static boolean isWindowFocused = false;
    public static boolean isBackPressed = false;
    static boolean flagmusic = false;
    static boolean returnMusicAfterAd = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ad
        String appKey = "af74c50960f92743e448bed283c4846c3430702632ebdfee";
        Appodeal.disableNetwork(this, "amazon_ads");
        Appodeal.disableNetwork(this, "applovin");
        Appodeal.disableNetwork(this, "mopub");
        Appodeal.disableNetwork(this, "inmobi");
        Appodeal.disableNetwork(this, "adcolony");
        Appodeal.disableNetwork(this, "vungle");
        Appodeal.disableNetwork(this, "facebook");
        Appodeal.disableNetwork(this, "startapp");
        Appodeal.disableNetwork(this, "liverail");
        Appodeal.disableNetwork(this, "cheetah");
        Appodeal.confirm(Appodeal.SKIPPABLE_VIDEO);
        Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL | Appodeal.SKIPPABLE_VIDEO);
        Appodeal.cache(PlayActivity.this,Appodeal.SKIPPABLE_VIDEO,1);

        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 1);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool.load(this, R.raw.click, 2);
        soundPool.load(this, R.raw.wrong0sound, 2);

        setContentView(R.layout.play);
        initViewElement();
        fixGUI();
        createQuestionBank();
        setNewQuestion();
        if (Player.getInstance().haveBonusLive) bonusLifeBtn.setBackground(getResources().getDrawable(R.drawable.heartright));

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 1);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool.load(this, R.raw.click, 2);
        soundPool.load(this, R.raw.wrong0sound, 2);

        Intent i = new Intent(this, MusicService.class);
        if (Player.getInstance().music && returnMusicAfterAd) this.startService(i);
        returnMusicAfterAd =false;
        flagmusic = false;
        setNewQuestion();
    }

    @Override
    protected void onResume() {
        Intent i = new Intent(this, MusicService.class);
        if(returnMusicAfterAd) this.startService(i);
        returnMusicAfterAd =false;
        super.onResume();
    }

    public void tapImage(View view) {
        if (Player.getInstance().music) soundPool.play(1, 0.7f, 0.7f, 0, 0, 1);
        okbtn.setVisibility(View.VISIBLE);
        cancelbtn.setVisibility(View.VISIBLE);
        if (view.getContext().getResources().getResourceName(view.getId()).contains(truAnswerBtnName)) ugadal = true;
        else ugadal = false;
        lockImageBtn(true);
        countView.setText(getResources().getString(R.string.youAreSure));
    }

    public void okbtnClick(View view) throws InterruptedException {
        if (ugadal) {
            if (Player.getInstance().music) soundPool.play(1, 0.7f, 0.7f, 0, 0, 1);
            Player.getInstance().trueQuestion++;
            countView.setText(getResources().getString(R.string.conterTrueAnswer) + " " + Player.getInstance().trueQuestion);
            Player.getInstance().questionsBank.remove(idQ);
            if (Player.getInstance().questionsBank.isEmpty()) {
                textquestion.setText(R.string.winnerText);
                questionLayout.setVisibility(View.VISIBLE);
                needRestart = true;
                if (Player.getInstance().topGameCount < Player.getInstance().trueQuestion)
                    Player.getInstance().topGameCount = Player.getInstance().trueQuestion;
                Player.getInstance().trueQuestion = 0;
                Player.getInstance().gameCount++;
                idQ = -1;
            } else {
                setNewQuestion();
            }

        } else {
            if (Player.getInstance().music) soundPool.play(2, 0.7f, 0.7f, 0, 0, 1);
            if (Player.getInstance().haveBonusLive) {
                Toast toast = new Toast(this);
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toastlayout));
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Player.getInstance().haveBonusLive = false;
                bonusLifeBtn.setBackground(getResources().getDrawable(R.drawable.heartbroken));
                lockImageBtn(false);
            } else {
                textquestion.setText(R.string.loserText);
                lifeOne.setBackground(getResources().getDrawable(R.drawable.heartbroken));
                questionLayout.setVisibility(View.VISIBLE);
                needRestart = true;
                if (Player.getInstance().topGameCount < Player.getInstance().trueQuestion)
                    Player.getInstance().topGameCount = Player.getInstance().trueQuestion;
                Player.getInstance().trueQuestion = 0;
                Player.getInstance().gameCount++;
                idQ = -1;
                if(Player.getInstance().gameCount % 4 == 0 && hasConnection(getBaseContext())) {Appodeal.show(this, Appodeal.INTERSTITIAL); if(Player.getInstance().music)returnMusicAfterAd=true;}
            }
        }

        ugadal = false;
        okbtn.setVisibility(View.GONE);
        cancelbtn.setVisibility(View.GONE);
    }

    public void cancelClick(View view) {
        if (Player.getInstance().music) soundPool.play(1, 0.7f, 0.7f, 0, 0, 1);
        okbtn.setVisibility(View.GONE);
        cancelbtn.setVisibility(View.GONE);
        lockImageBtn(false);
        countView.setText(getResources().getString(R.string.conterTrueAnswer) + " " + Player.getInstance().trueQuestion);
    }

    public void bonusLifeClick(View view) {
        if (Player.getInstance().music) soundPool.play(1, 0.7f, 0.7f, 0, 0, 1);
        if(hasConnection(getBaseContext())){
            if(Appodeal.isLoaded(Appodeal.SKIPPABLE_VIDEO)) Appodeal.show(PlayActivity.this, Appodeal.SKIPPABLE_VIDEO);
            else Appodeal.show(PlayActivity.this,Appodeal.INTERSTITIAL);

            Player.getInstance().haveBonusLive = true;
            bonusLifeBtn.setClickable(false);
            bonusLifeBtn.setBackground(getResources().getDrawable(R.drawable.heartright));
            if(Player.getInstance().music) returnMusicAfterAd =true;
        }else {
            Toast toast = new Toast(this);
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toastlayout));
            ((TextView) layout.findViewById(R.id.textView)).setText(R.string.hasntInternet);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }





    public void okClickfromtext(View view) {
        if (Player.getInstance().music) soundPool.play(1, 0.7f, 0.7f, 0, 0, 1);
        if (needRestart) {
            Intent i = new Intent(this, this.getClass());
            needRestart = false;

            finish();
            this.startActivity(i);

        }
        questionLayout.setVisibility(View.GONE);
        lockImageBtn(false);
    }

    private void setNewQuestion() {
        if (Player.getInstance().actualIdQuestion == -1) {
            this.idQ = random.nextInt(Player.getInstance().questionsBank.size());
        } else {
            idQ = Player.getInstance().actualIdQuestion;
            Player.getInstance().actualIdQuestion = -1;
            countView.setText(getResources().getString(R.string.conterTrueAnswer) + " " + Player.getInstance().trueQuestion);
        }
        String packageName = getPackageName();
        Log.i("Intuitor", "idq ravno = " + idQ);
        Log.i("Intuitor", "Player.getInstance().questionsBank.size() = " + Player.getInstance().questionsBank.size());
        textquestion.setText(Player.getInstance().questionsBank.get(idQ).questionText);
        repeatQ.setText(Player.getInstance().questionsBank.get(idQ).questionText);
        questionLayout.setVisibility(View.VISIBLE);

        clearImageButton(imageOne);
        clearImageButton(imageTwo);
        clearImageButton(imageThree);
        clearImageButton(imageFour);

        System.gc();


        class LoadImageAsyncTask extends AsyncTask<Context, Integer, Drawable[]> {
            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected Drawable[] doInBackground(Context... params) {
                Drawable[] drawables = new Drawable[4];
                try {
                    InputStream ims1 = params[0].getAssets().open(Player.getInstance().questionsBank.get(idQ).urlImg1true + ".jpg");
                    InputStream ims2 = params[0].getAssets().open(Player.getInstance().questionsBank.get(idQ).urlImg2 + ".jpg");
                    InputStream ims3 = params[0].getAssets().open(Player.getInstance().questionsBank.get(idQ).urlImg3 + ".jpg");
                    InputStream ims4 = params[0].getAssets().open(Player.getInstance().questionsBank.get(idQ).urlImg4 + ".jpg");

                    Drawable b1 = Drawable.createFromStream(ims1, null);
                    Drawable b2 = Drawable.createFromStream(ims2, null);
                    Drawable b3 = Drawable.createFromStream(ims3, null);
                    Drawable b4 = Drawable.createFromStream(ims4, null);

                    drawables[0] = b1;
                    drawables[1] = b2;
                    drawables[2] = b3;
                    drawables[3] = b4;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }


                return drawables;
            }

            @Override
            protected void onPostExecute(Drawable[] drawable) {
                super.onPostExecute(drawable);
                switch (random.nextInt(4) + 1) {
                    case 1:
                        imageOne.setBackground(drawable[0]);
                        imageTwo.setBackground(drawable[1]);
                        imageThree.setBackground(drawable[2]);
                        imageFour.setBackground(drawable[3]);
                        truAnswerBtnName = "imageone";
                        break;
                    case 2:
                        imageOne.setBackground(drawable[3]);
                        imageTwo.setBackground(drawable[0]);
                        imageThree.setBackground(drawable[1]);
                        imageFour.setBackground(drawable[2]);
                        truAnswerBtnName = "imagetwo";
                        break;
                    case 3:
                        imageOne.setBackground(drawable[2]);
                        imageTwo.setBackground(drawable[3]);
                        imageThree.setBackground(drawable[0]);
                        imageFour.setBackground(drawable[1]);
                        truAnswerBtnName = "imageThree";
                        break;
                    case 4:
                        imageOne.setBackground(drawable[1]);
                        imageTwo.setBackground(drawable[2]);
                        imageThree.setBackground(drawable[3]);
                        imageFour.setBackground(drawable[0]);
                        truAnswerBtnName = "imageFour";
                }
            }
        }

        LoadImageAsyncTask task = new LoadImageAsyncTask();
        task.execute(getBaseContext());
        lockImageBtn(true);

//        switch (random.nextInt(4) + 1) {
//            case 1:
//                imageOne.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg1true, "drawable", packageName)));
//                truAnswerBtnName = "imageone";
//                imageTwo.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg2, "drawable", packageName)));
//                imageThree.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg3, "drawable", packageName)));
//                imageFour.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg4, "drawable", packageName)));
//                break;
//            case 2:
//                imageTwo.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg1true, "drawable", packageName)));
//                truAnswerBtnName = "imagetwo";
//                imageOne.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg4, "drawable", packageName)));
//                imageThree.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg2, "drawable", packageName)));
//                imageFour.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg3, "drawable", packageName)));
//                break;
//            case 3:
//                imageThree.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg1true, "drawable", packageName)));
//                truAnswerBtnName = "imageThree";
//                imageOne.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg4, "drawable", packageName)));
//                imageTwo.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg2, "drawable", packageName)));
//                imageFour.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg3, "drawable", packageName)));
//                break;
//            case 4:
//                imageFour.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg1true, "drawable", packageName)));
//                truAnswerBtnName = "imageFour";
//                imageOne.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg2, "drawable", packageName)));
//                imageThree.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg4, "drawable", packageName)));
//                imageTwo.setBackgroundResource((getResources().getIdentifier(Player.getInstance().questionsBank.get(idQ).urlImg3, "drawable", packageName)));
//                break;
//        }
    }


    public void createQuestionBank() {
        if (Player.getInstance().actualIdQuestion != -1) return;
        String packageName = getPackageName();
        int i = getResources().getInteger(R.integer.countquestions);

        Player.getInstance().questionsBank.clear();
        for (int j = 1; j <= i; j++) {
            String[] array = getResources().getStringArray(getResources().getIdentifier("question" + j, "array", packageName));
            Question q = new Question(array[0], array[1], array[2], array[3], array[4], array[5], array[6]);
            Player.getInstance().questionsBank.add(q);

        }
    }

    private void lockImageBtn(boolean b) {
        if (b) {
            imageOne.setClickable(false);
            imageTwo.setClickable(false);
            imageThree.setClickable(false);
            imageFour.setClickable(false);
        } else {
            imageOne.setClickable(true);
            imageTwo.setClickable(true);
            imageThree.setClickable(true);
            imageFour.setClickable(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Player.getInstance().actualIdQuestion = idQ;
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(openFileOutput("Save", MODE_PRIVATE));
            objectOutputStream.writeObject(Player.getInstance());
            objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStop() {
        applicationdidenterbackground();
        super.onStop();
    }



    private void fixGUI() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        imageOne.getLayoutParams().height = metrics.heightPixels / 3;
        imageOne.getLayoutParams().width = (int) (metrics.widthPixels / 2.5f);

        imageTwo.getLayoutParams().height = metrics.heightPixels / 3;
        imageTwo.getLayoutParams().width = (int) (metrics.widthPixels / 2.5f);

        imageThree.getLayoutParams().height = metrics.heightPixels / 3;
        imageThree.getLayoutParams().width = (int) (metrics.widthPixels / 2.5f);

        imageFour.getLayoutParams().height = metrics.heightPixels / 3;
        imageFour.getLayoutParams().width = (int) (metrics.widthPixels / 2.5f);

        okbtn.getLayoutParams().height = metrics.heightPixels / 3;
        okbtn.getLayoutParams().width = metrics.heightPixels / 3;
        cancelbtn.getLayoutParams().height = metrics.heightPixels / 3;
        cancelbtn.getLayoutParams().width = metrics.heightPixels / 3;

        textquestion.setWidth((int) (metrics.widthPixels / 1.8f));
        textquestion.setHeight((int) (metrics.heightPixels / 1.8f));

        okbtnfortext.getLayoutParams().height = metrics.heightPixels / 5;
        okbtnfortext.getLayoutParams().width = metrics.heightPixels / 5;

        lifeOne.getLayoutParams().height = metrics.heightPixels / 7;
        lifeOne.getLayoutParams().width = (int)((metrics.heightPixels / 7)*1.2f);
        bonusLifeBtn.getLayoutParams().height = metrics.heightPixels / 7;
        bonusLifeBtn.getLayoutParams().width = (int)((metrics.heightPixels / 7)*1.2f);

        Typeface font_style = Typeface.createFromAsset(getAssets(), "9967.ttf");
        countView.setTypeface(font_style);
        textquestion.setTypeface(font_style);
        repeatQ.setTypeface(font_style);

        if (metrics.heightPixels >= 1100) {
            countView.setTextSize(45);
            textquestion.setTextSize(40);
            repeatQ.setTextSize(40);
        }
        if (metrics.heightPixels >= 1500) {
            countView.setTextSize(70);
            textquestion.setTextSize(55);
            repeatQ.setTextSize(55);
        }


    }

    private void initViewElement() {
        questionLayout = (RelativeLayout) findViewById(R.id.questionttayout);
        textquestion = (TextView) findViewById(R.id.textquestion);
        okbtnfortext = (ImageButton) findViewById(R.id.okbtnfortext);
        imageOne = (ImageButton) findViewById(R.id.imageone);
        imageTwo = (ImageButton) findViewById(R.id.imagetwo);
        imageThree = (ImageButton) findViewById(R.id.imageThree);
        imageFour = (ImageButton) findViewById(R.id.imageFour);
        okbtn = (ImageButton) findViewById(R.id.okbtn);
        cancelbtn = (ImageButton) findViewById(R.id.cancelbtn);
        countView = (TextView) findViewById(R.id.countView);
        bonusLifeBtn = (ImageButton) findViewById(R.id.Lifetwo);
        lifeOne = (ImageView) findViewById(R.id.lifeone);
        repeatQ = (TextView) findViewById(R.id.repeatquestion);

    }

    public static void clearImageButton(ImageButton imageButton) {
        if (!(imageButton.getDrawable() instanceof BitmapDrawable)) {
            return;
        }
        Bitmap drawable = (((BitmapDrawable) imageButton.getBackground())).getBitmap();
        if (drawable != null && !drawable.isRecycled()) {
            drawable.recycle();
        }
        imageButton.setImageDrawable(null);
    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        isWindowFocused = hasFocus;
        Log.e("Is Window Focus", "" + isWindowFocused);
        if (isBackPressed && !hasFocus) {
            isBackPressed = false;
            isWindowFocused = true;
        }
        if(!isWindowFocused && !isBackPressed){
            Intent i = new Intent(this, MusicService.class);
            if(!flagmusic)this.stopService(i);
        }

        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onBackPressed() {

            isBackPressed = true;


        super.onBackPressed();
    }

    public void applicationdidenterbackground() {

        if (!isWindowFocused) {

            isAppWentToBg = true;

            Intent i = new Intent(this, MusicService.class);
            if(!flagmusic)this.stopService(i);
        }
    }
    @Override
    protected void onStart() {
        flagmusic = false;
        applicationWillEnterForeground();

        super.onStart();
    }

    private void applicationWillEnterForeground() {
        if (isAppWentToBg) {
            //Google Analytics
            isAppWentToBg = false;

            Intent i = new Intent(this, MusicService.class);
            if (Player.getInstance().music) this.startService(i);
        }
    }

    @Override
    public void finish() {
        flagmusic = true;
        super.finish();
        overridePendingTransition(R.anim.alfa0sun,R.anim.for0faid0play);
    }


}
