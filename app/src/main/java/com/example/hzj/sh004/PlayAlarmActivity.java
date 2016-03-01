package com.example.hzj.sh004;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;

public class PlayAlarmActivity extends Activity {
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_alarm);
        mediaPlayer = MediaPlayer.create(this,R.raw.fob);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
