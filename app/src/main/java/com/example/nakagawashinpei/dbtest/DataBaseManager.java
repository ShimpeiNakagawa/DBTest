package com.example.nakagawashinpei.dbtest;

import io.realm.Realm;
import io.realm.RealmResults;

public class DataBaseManager {

    private Realm mRealm;

    public int getgrade(){
        int grade[];


        mRealm = Realm.getDefaultInstance();
        RealmResults<Schedule> results = mRealm.where(Schedule.class).findAll();
        return 0;

    }

}
