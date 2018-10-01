package com.example.nakagawashinpei.dbtest;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public interface DBManager {

    List getListAll();
    List getListBetweenPeriod(Date startDate, Date endDate);
    List getQuerrListyAll(String key);
    List getListyBetweenPeriod(String key, Date startDate, Date endDate);


}
