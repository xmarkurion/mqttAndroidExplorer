package com.example.dogsenseee;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DogData implements Parcelable {
    private String TAG = "DogDataClass";
    private HashMap<Timestamp, Integer> dataHashMap;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DogData(){
        this.dataHashMap = new HashMap<>();
    }

    protected DogData(Parcel in) {
        TAG = in.readString();
    }

    public static final Creator<DogData> CREATOR = new Creator<DogData>() {
        @Override
        public DogData createFromParcel(Parcel in) {
            return new DogData(in);
        }

        @Override
        public DogData[] newArray(int size) {
            return new DogData[size];
        }
    };

    public HashMap<Timestamp, Integer> getDataHashMap(){
        return this.dataHashMap;
    }

    public void logPrintHashMapItems(){

        dataHashMap.forEach( (time,value) ->{
            Log.d(TAG,sdf.format(time) + " -> " + value );
        });
    }

    public void addTimeAndData(Timestamp time, int data){
        this.dataHashMap.put(time,data);
    }

    public void addNow(int data){
        Date date = new Date();
        Timestamp now = new Timestamp(date.getTime());
        this.addTimeAndData(now,data);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(TAG);
    }
}
