package com.example.nakagawashinpei.dbtest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class ScheduleEditActivity extends AppCompatActivity {

    private Realm mRealm;
    private Realm GymRealm;
    EditText mDataEdit;
    EditText mTitleEdit;
    EditText mDetailEdit;
    Button mDelete;
    EditText mGrade5;
    Spinner mSpinner;
    private int mGrade5Counter = 0;
    EditText mGrade4;
    private int mGrade4Counter = 0;
    EditText mGrade3;
    private int mGrade3Counter = 0;
    EditText mGrade2;
    private int mGrade2Counter = 0;
    EditText mGrade1;
    private int mGrade1Counter = 0;
    private final static String TAG = ScheduleEditActivity.class.getSimpleName();
    String mGymName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);

        mRealm = Realm.getDefaultInstance();
        GymRealm = Realm.getDefaultInstance();
        mDataEdit = (EditText)findViewById(R.id.dateEdit);
        mDetailEdit = (EditText)findViewById(R.id.detailEdit);
        mDelete = (Button) findViewById(R.id.delete);
        mSpinner = (Spinner) findViewById(R.id.GymName); //スピナ データ選択時の処理
        mGrade5 = findViewById(R.id.grade5);
        mGrade4 = findViewById(R.id.grade4);
        mGrade3 = findViewById(R.id.grade3);
        mGrade2 = findViewById(R.id.grade2);
        mGrade1 = findViewById(R.id.grade1);

        RealmResults<Gym> Gymresults;
        Gymresults = GymRealm.where(Gym.class).findAll();
        String[] gym =new String[Gymresults.size()];
        for (int i = 0; i < Gymresults.size(); i++){
            gym[i] = Gymresults.get(i).getGym();
            Log.d("nakagawa", "GymName : " + gym[i]);
        }
        setSpinner(mSpinner,gym);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        long scheduleId = getIntent().getLongExtra("schedule_id",-1);
        if (scheduleId != -1){
            RealmResults<Schedule> results = mRealm.where(Schedule.class).equalTo("id",scheduleId).findAll();
            Schedule schedule = results.first();
            String date = sdf.format(schedule.getData());
            mDataEdit.setText(date);
            for(int i = 0; i < gym.length; i++){
                if (schedule.getTitle().equals(gym[i])){
                    mSpinner.setSelection(i);
                    break;
                }
            }

            mDetailEdit.setText(schedule.getDetail());
            setAllGradeCount(schedule);
            mGrade5.setText(String.valueOf(schedule.getGrade5()));
            mGrade4.setText(String.valueOf(schedule.getGrade4()));
            mGrade3.setText(String.valueOf(schedule.getGrade3()));
            mGrade2.setText(String.valueOf(schedule.getGrade2()));
            mGrade1.setText(String.valueOf(schedule.getGrade1()));
            mDelete.setVisibility(View.VISIBLE);
        } else {
            mDelete.setVisibility(View.INVISIBLE);
            Date date = new Date();
            mDataEdit.setText(sdf.format(date));
        }

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //何も選択されなかった時の動作
            @Override
            public void onNothingSelected(AdapterView adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                //選択されたアイテム名と位置（index)を内部変数へ保存
                mGymName =  parent.getSelectedItem().toString();
                Log.d("nakagawa", "onItemSelected:"+ mGymName);
            }
        });
    }

    public void onSaveTapped(View view){
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd");
        Date dateParse = new Date();
        try{
            dateParse = sfd.parse(mDataEdit.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date date = dateParse;
        long scheduleId = getIntent().getLongExtra("schedule_id",-1);
        if (scheduleId != -1) {
            final RealmResults<Schedule> results = mRealm.where(Schedule.class).equalTo("id",scheduleId).findAll();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Schedule schedule = results.first();
                    schedule.setDate(date);
                    schedule.setTitle(mGymName);
                    schedule.setDetail(mDetailEdit.getText().toString());
                    schedule.setGrade5(mGrade5Counter);
                    schedule.setGrade4(mGrade4Counter);
                    schedule.setGrade3(mGrade3Counter);
                    schedule.setGrade2(mGrade2Counter);
                    schedule.setGrade1(mGrade1Counter);
                    Log.d(TAG, "5:" + mGrade5Counter + " 4:"+mGrade4Counter + " 3:"+mGrade3Counter + " 2:"+mGrade2Counter + " 1:"+mGrade1Counter);

                }
            });
            Snackbar.make(findViewById(android.R.id.content),"アップデートしました",Snackbar.LENGTH_LONG)
                    .setAction("戻る",new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            finish();;
                        }
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show();
        } else {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Number maxId = realm.where(Schedule.class).max("id");
                    long nextId = 0;
                    if (maxId != null) {
                        nextId = maxId.longValue() + 1;
                    }
                    Schedule schedule = realm.createObject(Schedule.class, new Long(nextId));
                    schedule.setDate(date);
                    schedule.setTitle(mGymName);
                    schedule.setDetail(mDetailEdit.getText().toString());
                    schedule.setGrade5(mGrade5Counter);
                    schedule.setGrade4(mGrade4Counter);
                    schedule.setGrade3(mGrade3Counter);
                    schedule.setGrade2(mGrade2Counter);
                    schedule.setGrade1(mGrade1Counter);
                    Log.d(TAG, "else 5:" + mGrade5Counter + " 4:"+mGrade4Counter + " 3:"+mGrade3Counter + " 2:"+mGrade2Counter + " 1:"+mGrade1Counter);
                }
            });
            Toast.makeText(this, "追加しました", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    public void onDeleteTapped(View view) {
        final long scheduleId = getIntent().getLongExtra("schedule_id",-1);
        if (scheduleId != -1) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Schedule schedule = realm.where(Schedule.class)
                            .equalTo("id",scheduleId).findFirst();
                    schedule.deleteFromRealm();
                }
            });
            finish();;
        }
    }

    public void setAllGradeCount(Schedule schedule){
        mGrade5Counter = schedule.getGrade5();
        mGrade4Counter = schedule.getGrade4();
        mGrade3Counter = schedule.getGrade3();
        mGrade2Counter = schedule.getGrade2();
        mGrade1Counter = schedule.getGrade1();
    }


    public List<String> getGymList(){
        RealmResults<Gym> results = GymRealm.where(Gym.class).findAll();
        List<String> list = new ArrayList<>();

        for(int i = 0; i < results.size(); i++){
            list.add(results.get(i).getGym());
        }

        return list;
    }

    public void getGymArray(){

    }

    private void setSpinner(Spinner spinner, String[] arr){
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }





    /**
     * ＋ーボタンの処理
     * @param view
     */
    public void onGrade5MinusTapped(View view){
        if(mGrade5Counter > 0) {
            mGrade5Counter--;
            mGrade5.setText(String.valueOf(mGrade5Counter));
        }
    }

    public void onGrade5PlusTapped(View view){
        mGrade5Counter++;
        mGrade5.setText(String.valueOf(mGrade5Counter));
    }

    public void onGrade4MinusTapped(View view){
        if(mGrade4Counter > 0) {
            mGrade4Counter--;
            mGrade4.setText(String.valueOf(mGrade4Counter));
        }
    }

    public void onGrade4PlusTapped(View view){
        mGrade4Counter++;
        mGrade4.setText(String.valueOf(mGrade4Counter));
    }

    public void onGrade3MinusTapped(View view){
        if(mGrade3Counter > 0) {
            mGrade3Counter--;
            mGrade3.setText(String.valueOf(mGrade3Counter));
        }
    }

    public void onGrade3PlusTapped(View view){
        mGrade3Counter++;
        mGrade3.setText(String.valueOf(mGrade3Counter));
    }

    public void onGrade2MinusTapped(View view){
        if(mGrade2Counter > 0) {
            mGrade2Counter--;
            mGrade2.setText(String.valueOf(mGrade2Counter));
        }
    }

    public void onGrade2PlusTapped(View view){
        mGrade2Counter++;
        mGrade2.setText(String.valueOf(mGrade2Counter));
    }

    public void onGrade1MinusTapped(View view){
        if(mGrade1Counter > 0) {
            mGrade1Counter--;
            mGrade1.setText(String.valueOf(mGrade1Counter));
        }
    }

    public void onGrade1PlusTapped(View view){
        mGrade1Counter++;
        mGrade1.setText(String.valueOf(mGrade1Counter));
    }

    public void onMemoryButtonTapped(View view){
        startActivity(new Intent(ScheduleEditActivity.this,MainActivity.class));
        finish();
    }

    public void onChartButtonTapped(View view){
        startActivity(new Intent(ScheduleEditActivity.this,Chart.class));
        finish();
    }

    public void onAddGymButtonTapped(View view){
        startActivity(new Intent(ScheduleEditActivity.this,AddGymActivity.class));
        finish();
    }
}
