package com.example.dogsenseee;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dogsenseee.Markurion.mqttEngine;

import org.eclipse.paho.android.service.MqttAndroidClient;


import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView status, uptime;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> dataList;
    private ListView list;
    private Button in, out, on,off;
    private static final String TAG = "MainScreen";
    private String topic, clientId, serverURI, username="pi", password="pi";
    private MqttAndroidClient client;
    private mqttEngine M;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();
        status = findViewById(R.id.textView_connection);
        uptime = findViewById(R.id.textView_status);
        list = findViewById(R.id.list);
        in = findViewById(R.id.main_btn);
        on = findViewById(R.id.main_btn3);
        off = findViewById(R.id.main_btn4);

        on.setEnabled(false);
        off.setEnabled(false);
        status.setText("Not connected..");

        dataList = new ArrayList<>();
        dataList.add("Messages will be displayed here.");

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataList);
        list.setAdapter(arrayAdapter);

       list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
       list.setStackFromBottom(true);

        M = new mqttEngine(
                arrayAdapter,
                getApplicationContext(),
                "Android_Device",
                "tcp://192.168.2.196:1883",
                "pi",
                "pi");

//        M = new mqttEngine(
//                arrayAdapter,
//                getApplicationContext(),
//                "Android_Device",
//                "tcp://localhost:1883");

        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimer();
//                M.setSubTopic("board/display/#");
                M.setSubTopic("#");
                M.start();
            }
        });

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.ledOn();
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.ledOff();
            }
        });
    }

    private void setTimer(){
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(M.getStatus()){
                            on.setEnabled(true);
                            off.setEnabled(true);
                            status.setText("Connected to: \n" + M.getSeverURI());
                            uptime.setText("Server uptime: " + String.format("%.1f",M.getUptime()) + " min");
                            t.cancel();
                        }
                    }
                });
            }
        };
        t.schedule(tt,0,200);
    }
}