package com.example.hzj.sh004;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hzj on 16-2-6.
 */
public class TimerView extends LinearLayout{
    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                btnPause.setVisibility(GONE);
                btnResume.setVisibility(VISIBLE);
            }
        });
        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                etHour.setText("00");
                etMinute.setText("00");
                etSecond.setText("00");
                btnReset.setVisibility(GONE);
                btnResume.setVisibility(GONE);
                btnPause.setVisibility(GONE);
                btnStart.setVisibility(VISIBLE);
            }
        });
        btnResume = (Button) findViewById(R.id.btnResume);
        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                btnResume.setVisibility(GONE);
                btnPause.setVisibility(VISIBLE);
            }
        });
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();

                btnStart.setVisibility(GONE);
                btnPause.setVisibility(VISIBLE);
                btnReset.setVisibility(VISIBLE);
            }
        });
        etHour = (EditText) findViewById(R.id.etHour);
        etMinute = (EditText) findViewById(R.id.etMinute);
        etSecond = (EditText) findViewById(R.id.etSecond);
        etHour.setText("00");
        etHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    int valve = Integer.parseInt(s.toString());
                    if (valve > 59) {
                        etHour.setText("59");
                    } else if (valve < 0) {
                        etHour.setText("0");
                    }
                }
                checkToEnableBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etMinute.setText("00");
        etMinute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)) {
                    int valve = Integer.parseInt(s.toString());
                    if (valve > 59) {
                        etMinute.setText("59");
                    } else if (valve < 0) {
                        etMinute.setText("0");
                    }
                }
                checkToEnableBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSecond.setText("00");
        etSecond.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    int valve = Integer.parseInt(s.toString());
                    if (valve > 59) {
                        etSecond.setText("59");
                    } else if (valve < 0) {
                        etSecond.setText("0");
                    }
                }
                checkToEnableBtn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnStart.setVisibility(VISIBLE);
        btnStart.setEnabled(false);
        btnResume.setVisibility(GONE);
        btnPause.setVisibility(GONE);
        btnReset.setVisibility(GONE);
    }
    private void checkToEnableBtn(){
        btnStart.setEnabled(!TextUtils.isEmpty(etHour.getText())&&Integer.parseInt(etHour.getText().toString())>0||
                (!TextUtils.isEmpty(etMinute.getText())&&Integer.parseInt(etMinute.getText().toString())>0)||
                        (!TextUtils.isEmpty(etSecond.getText())&&Integer.parseInt(etSecond.getText().toString())>0));
    }
    private Button btnStart,btnResume,btnPause,btnReset;
    private EditText etHour,etMinute,etSecond;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private int allTimerCount;
    private static final int MSG_WHAT_TIME_IS_UP = 1;
    private static final int MST_WHAT_TIME_TICK = 2;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_WHAT_TIME_IS_UP:
                    new AlertDialog.Builder(getContext()).setTitle("Time is up").setMessage("Times up").setNegativeButton("Cancel",null);
                    btnReset.setVisibility(GONE);
                    btnResume.setVisibility(GONE);
                    btnPause.setVisibility(GONE);
                    btnStart.setVisibility(VISIBLE);
                    break;
                case MST_WHAT_TIME_TICK:
                    int hour = allTimerCount/60/60;
                    int minute = (allTimerCount/60)%60;
                    int second = allTimerCount%60;
                    etHour.setText(""+hour);
                    etMinute.setText(""+minute);
                    etSecond.setText(""+second);
                    break;
                default:
                    break;
            }
        }
    };
    private void startTimer(){
        if(timerTask== null){
            allTimerCount = Integer.parseInt(etHour.getText().toString())*60*60+Integer.parseInt(etMinute.getText().toString())*60+Integer.parseInt(etSecond.getText().toString());
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    allTimerCount--;
                    handler.sendEmptyMessage(MST_WHAT_TIME_TICK);
                    if(allTimerCount<=0){
                        handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
                        stopTimer();
                    }
                }
            };
            timer.schedule(timerTask,1000,1000);
        }
    }
    private void stopTimer(){
        if(timerTask!=null){
            timerTask.cancel();
            timerTask=null;
        }
    }
}
