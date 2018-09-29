package com.example.nakagawashinpei.dbtest;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddGymActivity extends AppCompatActivity {

    private Realm mRealm;
    EditText mGymText;
    RealmResults<Gym> results;
    private String spinnerGym;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gym);




        mRealm = Realm.getDefaultInstance();
        results = mRealm.where(Gym.class).findAll();
        spinner = (Spinner) findViewById(R.id.GymSpineer);

        mGymText = findViewById(R.id.editText);



        int b = results.size();
        Log.d("nakagawa", "onCreate int : " + b);

        if (results != null){
            //Toast.makeText(this, b ,Toast.LENGTH_SHORT).show();
            String[] gym =new String[results.size()];
            for (int i = 0; i < results.size(); i++){
                gym[i] = results.get(i).getGym();
                Log.d("nakagawa", "GymName : " + gym[i]);
            }
            setSpinner(spinner,gym);
        }
        else {
            Chart chart = new Chart();
            chart.toast("results == null");
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //何も選択されなかった時の動作
            @Override
            public void onNothingSelected(AdapterView adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                //選択されたアイテム名と位置（index)を内部変数へ保存
                spinnerGym =  parent.getSelectedItem().toString();
                Log.d("", "onItemSelected:"+ spinnerGym);
            }
        });

    }

    private void setSpinner(Spinner spinner, String[] arr){
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    public void onMemoryButtonTapped(View view){
        startActivity(new Intent(AddGymActivity.this,MainActivity.class));
        finish();
    }

    public void onChartButtonTapped(View view){
        startActivity(new Intent(AddGymActivity.this,Chart.class));
        finish();
    }

    public void onAddGymButtonTapped(View view){
        startActivity(new Intent(AddGymActivity.this,AddGymActivity.class));
        finish();
    }

    public void AddGymButtonTapped(View view){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number maxId = realm.where(Gym.class).max("id");
                long nextId = 0;
                if (maxId != null) {
                    nextId = maxId.longValue() + 1;
                }
                Gym gym = realm.createObject(Gym.class, new Long(nextId));
                String gymText = mGymText.getText().toString();
                if(!gymText.isEmpty()){
                    Log.d("nakagawa", "AddGym : " + gymText);
                    gym.setGym(gymText);
                    mGymText.getEditableText().clear();
                    upDateSpinner();
                }
                else {
                    Log.d("nakagawa", "Error Text is Empty");
                }
            }
        });
        Toast.makeText(this, "追加しました", Toast.LENGTH_SHORT).show();
    }


    public void GymButtonTapped(View view){
        new AlertDialog.Builder(AddGymActivity.this)
                .setTitle("本当に削除しますか？")
                .setPositiveButton(
                        "はい",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete();
                                upDateSpinner();
                            }
                        })
                .setNegativeButton(
                        "いいえ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .show();
    }

    public void delete(){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Gym gym =realm.where(Gym.class).equalTo("Gym",spinnerGym).findFirst();
                gym.deleteFromRealm();

                /*
                results = mRealm.where(Gym.class).findAll();
                int b = results.size();
                Log.d("nakagawa", "onCreate int : " + b);

                if (results != null){
                    //Toast.makeText(this, b ,Toast.LENGTH_SHORT).show();
                    String[] gym1 =new String[results.size()];
                    for (int i = 0; i < results.size(); i++){
                        gym1[i] = results.get(i).getGym();
                        Log.d("nakagawa", "GymName : " + gym1[i]);
                    }
                    setSpinner(spinner,gym1);
                }
                else {
                    Chart chart = new Chart();
                    chart.toast("results == null");
                }
                */
            }
        });
    }

    public void upDateSpinner(){
        results = mRealm.where(Gym.class).findAll();
        int b = results.size();

        if (results != null){
            //Toast.makeText(this, b ,Toast.LENGTH_SHORT).show();
            String[] gym1 =new String[results.size()];
            for (int i = 0; i < results.size(); i++){
                gym1[i] = results.get(i).getGym();
                Log.d("nakagawa", "GymName : " + gym1[i]);
            }
            setSpinner(spinner,gym1);
        }
        else {
            Chart chart = new Chart();
            chart.toast("results == null");
        }
    }

    public void testButton(View view){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog dialog = new DatePickerDialog(this, R.style.MyDialog, DateSetListener, year, monthOfYear, dayOfMonth);
        dialog.show();

        /*
        DatePickerDialog datePickerDialog;
        //日付情報の初期設定
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        //日付設定ダイアログの作成
        datePickerDialog = new DatePickerDialog(this, DateSetListener, year, monthOfYear, dayOfMonth);
        //日付設定ダイアログの表示
        datePickerDialog.show();
        */

    }

    //日付設定時のリスナ登録
    DatePickerDialog.OnDateSetListener DateSetListener = new DatePickerDialog.OnDateSetListener(){
        public void onDateSet(android.widget.DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear++;
            Date testdate = null;
            String strDate;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            strDate = String.valueOf(year) + "/" + String.valueOf(monthOfYear) + "/" + String.valueOf(dayOfMonth);
            try {
                testdate = sdf.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //ログ出力
            Log.d("DatePicker","year:" +year + " monthOfYear:" + monthOfYear + " dayOfMonth:" + dayOfMonth + " Date: " + testdate);
        }
    };
}
