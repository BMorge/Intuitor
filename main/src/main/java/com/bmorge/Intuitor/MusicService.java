package com.bmorge.Intuitor;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * Created by Dexp on 12.10.2016.
 */
public class MusicService extends Service {
    MediaPlayer mediaPlayer;
//    public MusicService() {
//        super("MusicService"); for IntentService
//    }

    @Override
    public void onCreate() {
        mediaPlayer = MediaPlayer.create(this, R.raw.backgraud0music);
        mediaPlayer.setLooping(true);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

//    @Override
//    protected void onHandleIntent(Intent intent) {
//        mediaPlayer.start();
//    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
