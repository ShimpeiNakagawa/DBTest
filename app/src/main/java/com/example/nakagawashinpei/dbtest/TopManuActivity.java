package com.example.nakagawashinpei.dbtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class TopManuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_manu);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }


    public void onMemoryButtonTapped(View view){
        startActivity(new Intent(TopManuActivity.this,MainActivity.class));
        finish();
    }

    public void onChartButtonTapped(View view){
        startActivity(new Intent(TopManuActivity.this,Chart.class));
        finish();
    }

    public void onAddGymButtonTapped(View view){
        startActivity(new Intent(TopManuActivity.this,AddGymActivity.class));
        finish();
    }
}
