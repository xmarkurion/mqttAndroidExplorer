package com.example.dogsenseee;
import android.util.Log;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DogDataSerial implements Serializable {
    private String TAG = "DogDataClass";
    private HashMap<Timestamp, Integer> dataHashMap;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DogDataSerial(){
        this.dataHashMap = new HashMap<>();
    }

    public HashMap<Timestamp, Integer> getDataHashMap(){
        return this.dataHashMap;
    }

    public void logPrintHashMapItems(){
        dataHashMap.forEach( (time,value) ->{
            Log.d(TAG,sdf.format(time) + " -> " + value );
        });
    }

    public void addTimeAndData(Timestamp time, int data){
        int a = this.dataHashMap.put(time,data);
        Log.d(TAG, " Add time of: " + time + "  data: " + data + " result: " + a);
    }

    public void addNow(int data){
        Date date = new Date();
        Timestamp now = new Timestamp(date.getTime());
        this.addTimeAndData(now,data);
    }

}
