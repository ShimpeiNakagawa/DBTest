package com.example.nakagawashinpei.dbtest;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ScheduleManager implements DBManager{

    private static String TAG = "ScheduleManager";
    private Realm mRealm;
    private RealmResults<Schedule> mResults;


    public ScheduleManager() {
        mRealm = Realm.getDefaultInstance();
    }


    @Override
    public List<Schedule> getListAll() {
        List<Schedule> ScheduleList = new ArrayList<Schedule>();

        mResults = mRealm.where(Schedule.class).findAll();
        for (int i = 0; i < mResults.size(); i++){
            Log.d(TAG, "mResult: " + mResults.get(i));
            ScheduleList.add(mResults.get(i));
        }
        Log.d(TAG, "ScheduleList: " + ScheduleList);
        return ScheduleList;
    }

    @Override
    public List<Schedule> getListBetweenPeriod(Date startDate, Date endDate) {
        List<Schedule> ScheduleList = new ArrayList<Schedule>();

        Log.d(TAG, "startDate: "+ startDate + " endDate: " + endDate);

        if (startDate != null && endDate != null){
            mResults = mRealm.where(Schedule.class).findAll();
            for(int i = 0; i < mResults.size(); i++){
                if(startDate.compareTo(mResults.get(i).getData()) != 1 && endDate.compareTo(mResults.get(i).getData()) != -1){
                    Log.d(TAG, "mResult: " + mResults.get(i));
                    ScheduleList.add(mResults.get(i));
                    //
                }
            }
        }
        else {
            ScheduleList = getListAll();
        }
        //Log.d(TAG, "ScheduleList: " + ScheduleList);
        Log.d(TAG, "**************************************");
        return ScheduleList;
    }



    @Override
    public RealmResults getQuerrListyAll(String key) {
        return null;
    }

    @Override
    public RealmResults getListyBetweenPeriod(String key, Date startDate, Date endDate) {
        return null;
    }
}
