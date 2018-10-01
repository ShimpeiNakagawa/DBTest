package com.example.nakagawashinpei.dbtest;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class GymManager implements DBManager{

    private Realm mRealm;
    private static String TAG = "GymManager";
    private RealmResults<Gym> mResults;

    public GymManager() {
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public List getListAll() {
        List<Gym> GymList = new ArrayList<Gym>();

        mResults = mRealm.where(Gym.class).findAll();
        for (int i = 0; i < mResults.size(); i++){
            Log.d(TAG, "mResult: " + mResults.get(i));
            GymList.add(mResults.get(i));
        }
        Log.d(TAG, "ScheduleList: " + GymList);
        return GymList;
    }

    @Override
    public List getListBetweenPeriod(Date startDate, Date endDate) {
        List<Gym> GymList = new ArrayList<Gym>();

        mResults = mRealm.where(Gym.class).findAll();
        if (startDate == null && endDate == null){
            for(int i = 0; i < mResults.size(); i++){
                Log.d(TAG, "mResult: " + mResults.get(i));
                GymList.add(mResults.get(i));
            }
        }
        else {
            Log.d(TAG, "Error:startDate or endDate is null");
        }
        Log.d(TAG, "ScheduleList: " + GymList);
        return GymList;
    }

    @Override
    public List getQuerrListyAll(String key) {
        return null;
    }

    @Override
    public List getListyBetweenPeriod(String key, Date startDate, Date endDate) {
        return null;
    }
}
