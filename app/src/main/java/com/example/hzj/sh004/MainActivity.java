package com.example.hzj.sh004;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {
    private TabHost tabHost;
    private StopWatchView stopWatchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("tabTime").setIndicator("CLock").setContent(R.id.tabTime));
        tabHost.addTab(tabHost.newTabSpec("tabAlarm").setIndicator("NaoCLock").setContent(R.id.tabAlarm));
        tabHost.addTab(tabHost.newTabSpec("tabTimer").setIndicator("count").setContent(R.id.tabTimer));
        tabHost.addTab(tabHost.newTabSpec("tabStopWatch").setIndicator("miaoCLock").setContent(R.id.tabStopWatch));
        stopWatchView = (StopWatchView) findViewById(R.id.tabStopWatch);
    }

    @Override
    protected void onDestroy() {
        stopWatchView.onDestroy();
        super.onDestroy();
    }
}
