package com.example.hzj.sh004;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by hzj on 16-2-6.
 */
public class TimeView extends LinearLayout{
    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTime.setText("Hello");
        timerHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility==VISIBLE){
            timerHandler.sendEmptyMessage(0);
        }else{
            timerHandler.removeMessages(0);
        }
    }

    private void refreshTime(){
        Calendar c = Calendar.getInstance();
        tvTime.setText(String.format("%d:%d:%d",c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),c.get(Calendar.SECOND)));
    }
    private Handler timerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshTime();
            if(getVisibility()== View.VISIBLE) {
                timerHandler.sendEmptyMessageDelayed(0, 1000);
            }else{

            }
        }
    };
    private TextView tvTime;
}
