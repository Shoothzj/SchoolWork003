package com.example.hzj.sh004;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by hzj on 16-2-6.
 */
public class AlarmView extends LinearLayout{
    public AlarmView(Context context) {
        super(context);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init(){
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }
    private void saveAlarmList(){
        SharedPreferences.Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE).edit();
        StringBuffer sb  = new StringBuffer();
        for(int i=0;i<adapter.getCount();i++){
            sb.append(adapter.getItem(i).getTime()).append(",");
        }
        if(sb.length()>1) {
            String content = sb.toString().substring(0, sb.length() - 1);
            editor.putString(KEY_ALARM_LIST, content);
        }else{
            editor.putString(KEY_ALARM_LIST,null);
        }
        editor.commit();
    }
    private void readSavedAlarmList(){
        SharedPreferences sp = getContext().getSharedPreferences(AlarmView.class.getName(),Context.MODE_PRIVATE);
        String content = sp.getString(KEY_ALARM_LIST,null);
        if(content!=null){
            String[] timeStrings = content.split(",");
            for(String string:timeStrings){
                adapter.add(new AlarmDate(Long.parseLong(string)));
            }
        }
    }
    private static final String KEY_ALARM_LIST ="alrmList";
    private Button btnAddAlarm;
    private ListView lvAlarmList;
    private ArrayAdapter<AlarmDate> adapter;
    private AlarmManager alarmManager;
    private static class AlarmDate{
        private Calendar date;
        private long time;
        private String timeLabel = "";
        public AlarmDate(long time){
            this.time = time;
            date = Calendar.getInstance();
            date.setTimeInMillis(time);
            timeLabel = String.format("%d月%d日 %d:%d",date.get(Calendar.MONTH)+1,date.get(Calendar.DATE),date.get(Calendar.HOUR),date.get(Calendar.MINUTE));
        }
        public long getTime() {
            return time;
        }

        public String getTimeLabel() {
            return timeLabel;
        }

        @Override
        public String toString() {
            return getTimeLabel();
        }
        public int getId(){
            return (int)getTime()/1000/60;
        }
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        lvAlarmList = (ListView) findViewById(R.id.lvAlarmList);
        adapter = new ArrayAdapter<AlarmDate>(getContext(),android.R.layout.simple_list_item_1);
        lvAlarmList.setAdapter(adapter);
        readSavedAlarmList();
        btnAddAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm();
            }
        });
        lvAlarmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext()).setTitle("Options").setItems(new CharSequence[]{"Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                deleteAlarm(position);
                                break;
                            default:
                                break;
                        }
                    }
                }).setNegativeButton("Cancel", null).show();
            }
        });
    }
    private void deleteAlarm(int position){
        AlarmDate ad = adapter.getItem(position);
        adapter.remove(ad);
        saveAlarmList();
    }
    private void addAlarm(){
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND, 0);

                Calendar currentTime = Calendar.getInstance();
                if(calendar.getTimeInMillis()<=currentTime.getTimeInMillis()){
                    calendar.setTimeInMillis(calendar.getTimeInMillis()+24*60*60*1000);
                }
                AlarmDate ad = new AlarmDate(calendar.getTimeInMillis());
                adapter.add(ad);

                PendingIntent pd = PendingIntent.getBroadcast(getContext(),
                        ad.getId(),
                        new Intent(getContext(), AlarmReceiver.class),0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        ad.getTime(),
                        300000, pd);
                saveAlarmList();
            }
        },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),false).show();

    }
}
