package com.example.hzj.sh004;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hzj on 16-2-6.
 */
public class StopWatchView extends LinearLayout{
    public StopWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvHour = (TextView) findViewById(R.id.timeHour);
        tvHour.setText("0");
        tvMinute = (TextView) findViewById(R.id.timeMinute);
        tvMinute.setText("0");
        tvSecond = (TextView) findViewById(R.id.timeSecond);
        tvSecond.setText("0");
        tvMSecond = (TextView) findViewById(R.id.timeMSecond);
        tvMSecond.setText("0");
        btnStart = (Button) findViewById(R.id.btnSWStart);
        btnReset = (Button) findViewById(R.id.btnSWReset);
        btnResume = (Button) findViewById(R.id.btnSWResume);
        btnPause = (Button) findViewById(R.id.btnSWPause);
        btnLop = (Button) findViewById(R.id.btnSWLop);
        btnStart.setVisibility(VISIBLE);
        btnLop.setVisibility(GONE);
        btnPause.setVisibility(GONE);
        btnReset.setVisibility(GONE);
        btnResume.setVisibility(GONE);
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                btnStart.setVisibility(GONE);
                btnPause.setVisibility(VISIBLE);
                btnLop.setVisibility(VISIBLE);
            }
        });
        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                btnPause.setVisibility(GONE);
                btnResume.setVisibility(VISIBLE);
                btnLop.setVisibility(GONE);
                btnReset.setVisibility(VISIBLE);
            }
        });
        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.GONE);
                btnLop.setVisibility(View.VISIBLE);
            }
        });
        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                tenMSec = 0;
                adapter.clear();
                btnLop.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnReset.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);

            }
        });
        btnLop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.insert(String.format("%d:%d:%d.%d",tenMSec/100/60/60,(tenMSec/100/60)%60,(tenMSec/100)%60,tenMSec%100),0);
            }
        });
        lvTimeList = (ListView) findViewById(R.id.lvWatchTimeList);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1);
        lvTimeList.setAdapter(adapter);
        showTimeTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
            }
        };
        timer.schedule(showTimeTask,200,200);
    }
    private void startTimer(){
        if(timerTask==null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    tenMSec++;

                }
            };
            timer.schedule(timerTask,10,10);
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_WHAT_SHOW_TIME:
                    tvHour.setText(tenMSec/100/60/60+"");
                    tvMinute.setText((tenMSec/100/60)%60+"");
                    tvSecond.setText((tenMSec/100)%60+"");
                    tvMSecond.setText(tenMSec%100+"");
                    break;
            }
        }
    };
    private void stopTimer(){
        if(timerTask!=null){
            timerTask.cancel();
            timerTask = null;
        }
    }
    private static final int MSG_WHAT_SHOW_TIME=1;
    private TimerTask showTimeTask = null;
    private int tenMSec = 0;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private ArrayAdapter<String> adapter;
    private ListView lvTimeList;
    private TextView tvHour,tvMinute,tvSecond,tvMSecond;
    private Button btnStart,btnResume,btnReset,btnPause,btnLop;

    public void onDestroy() {
        timer.cancel();
    }
}
