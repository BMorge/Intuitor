package com.bmorge.Intuitor;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Dexp on 12.10.2016.
 */
public class BaseActivity extends Activity {
    protected boolean isNeedSmf = true;


    @Override
    protected void onStop() {

        super.onStop();
        if (isNeedSmf){
            Intent i = new Intent(this, MusicService.class);
            this.stopService(i);
        }

    }
}
