package com.example.dogsenseee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dogsenseee.Markurion.mqttEngine;

public class LedActivity extends AppCompatActivity {

    private mqttEngine M;
    private Button send,clear,scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
        M = getIntent().getParcelableExtra("MQTT");


        send = findViewById(R.id.led_btn_send);
        clear = findViewById(R.id.led_btn_cls);
        scroll = findViewById(R.id.led_btn_scroll);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.sendNewMessage("Testing","pico/MAX_LED/send");
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.sendNewMessage("a","pico/MAX_LED/clear");
            }
        });

        scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.sendNewMessage("Testing","pico/MAX_LED/scroll");
            }
        });



    }
}