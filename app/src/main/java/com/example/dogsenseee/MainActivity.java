package com.example.dogsenseee;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dogsenseee.Markurion.MqttEngine;
import org.eclipse.paho.android.service.MqttAndroidClient;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView status, uptime;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> dataList;
    private ListView list;
    private Button in, led, on,off;
    private static final String Tag = "MainScreen";
    private String topic, clientId, serverURI, username="pi", password="pi";
    private MqttAndroidClient client;
    private MqttEngine M;
    private DogDataSerial dogData;

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
        led = findViewById(R.id.main_btn2);
        off = findViewById(R.id.main_btn4);

        on.setEnabled(false);
        off.setEnabled(false);
        led.setEnabled(false);
        status.setText("Not connected..");

        dataList = new ArrayList<>();
        dataList.add("Messages will be displayed here.");

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataList);
        list.setAdapter(arrayAdapter);

       list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
       list.setStackFromBottom(true);

       dogData = new DogDataSerial();

        M = new MqttEngine(
                dogData,
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
                //M.setSubTopic("board/display/#");
                M.setSubTopic("#");
//                M.setSubTopic("$SYS/broker/store/messages/count");
//                M.setSubTopic("pico/led");
                M.start();
            }
        });

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.sendNewMessage("1","pico/led");
                M.sendNewMessage("true","board/display/backlight");
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.sendNewMessage("0","pico/led");
                M.sendNewMessage("false","board/display/backlight");
            }
        });

        led.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //M.sendNewMessage(Double.toString(M.getUptime()),"pico/MAX_LED/send");
                M.sendNewMessage(String.format("%.1f",M.getUptime()),"pico/MAX_LED/scroll");
                //M.sendNewMessage("","pico/MAX_LED/cls");
                Intent i = new Intent(MainActivity.this, LedActivity.class);

                int sizeOflist = dogData.getDataHashMap().size();
                Log.d(Tag, "Size of passed hash map is: " + sizeOflist);
                i.putExtra("dogData", dogData);
                startActivity(i);
            }
        });
    }

    private void setTimer(){
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        if(M.getStatus()){
                            on.setEnabled(true);
                            off.setEnabled(true);
                            led.setEnabled(true);
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