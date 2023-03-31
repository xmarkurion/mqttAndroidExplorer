package com.example.dogsenseee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.dogsenseee.Markurion.MqttEngine;

public class LedActivity extends AppCompatActivity {

    private DogDataSerial dogData;
    private Button send,clear,scroll;
    private String Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Tag = "LED_ACTIVITY";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);


        send = findViewById(R.id.led_btn_send);
        clear = findViewById(R.id.led_btn_cls);
        scroll = findViewById(R.id.led_btn_scroll);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = getIntent();
                    dogData = (DogDataSerial) i.getSerializableExtra("dogData");
                    Log.d(Tag, dogData.getDataHashMap().toString());
                    Log.d(Tag, String.valueOf(dogData.getDataHashMap().size()));
                }catch (Exception e){
                    Log.d(Tag, e.getMessage());
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}