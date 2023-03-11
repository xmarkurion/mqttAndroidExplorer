package com.example.dogsenseee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dogsenseee.Markurion.MqttEngine;

public class LedActivity extends AppCompatActivity {

    private DogData dogData;
    private Button send,clear,scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
        dogData = getIntent().getParcelableExtra("dogData");

        send = findViewById(R.id.led_btn_send);
        clear = findViewById(R.id.led_btn_cls);
        scroll = findViewById(R.id.led_btn_scroll);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dogData.logPrintHashMapItems();
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