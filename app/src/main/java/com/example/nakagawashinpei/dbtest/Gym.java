package com.example.nakagawashinpei.dbtest;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class Gym extends RealmObject {
    @PrimaryKey
    private long id;
    private String Gym;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGym() {
        return Gym;
    }

    public void setGym(String gym) {
        Gym = gym;
    }

}

