package com.example.hzj.sh004;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by hzj on 16-2-6.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("daw");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intnt = new Intent(context,PlayAlarmActivity.class);
        intnt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intnt);
    }
}
