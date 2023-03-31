package com.example.dogsenseee.Markurion;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.dogsenseee.DogData;
import com.example.dogsenseee.DogDataSerial;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;
import java.util.HashSet;

//https://youtu.be/BBLqa2Wh6nQ -> If you need info from where this came from.

public class MqttEngine {
    private DogDataSerial dogData;
    private ArrayAdapter<String> adapter;
    private String TAG = "MqttEngineClass";
    private final String clientID;
    private final String severURI;

    private double uptime;
    private String username;
    private String password;

    private HashSet<String> subTopics;

    private Context context;

    private MqttAndroidClient client;

    private boolean status;

    public MqttEngine(DogDataSerial dogData, ArrayAdapter<String> adapter, Context context, String clientId, String serverURI, String username, String password) {
        this.clientID = clientId;
        this.severURI = serverURI;
        this.username = username;
        this.password = password;
        this.context = context;
        this.adapter = adapter;
        this.dogData = dogData;
        common();
    }
//    public MqttEngine(DogData dogData, ArrayAdapter<String> adapter, Context context, String clientId, String serverURI) {
//            this.clientID = clientId;
//            this.severURI = serverURI;
//            this.username = "";
//            this.password = "";
//            this.context = context;
//            this.adapter = adapter;
//            this.dogData = dogData;
//            common();
//    }

    private void common(){
        this.status = false;
        this.uptime = 0;
        this.subTopics = new HashSet<>();
    }

    public boolean getStatus() {
        return status;
    }

    public double getUptime(){return uptime;}

    public String getSeverURI() {return severURI; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSubTopic(String topic) {
        subTopics.add(topic);
    }

    public void start() {
        client = new MqttAndroidClient(this.context, this.severURI, this.clientID);
        Log.d(TAG, "username: " + this.username);
        Log.d(TAG, "pass: " + this.password);
        Log.d(TAG, "topics " + this.subTopics.toString());
        Log.d(TAG, "server: " + this.severURI);
        Log.d(TAG, "clientID: " + this.clientID);
        connect();
    }

    private MqttConnectOptions setupOptions() {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(this.username);
        connOpts.setPassword(this.password.toCharArray());
        return connOpts;
    }

    private void connect() {
        IMqttToken token;
        try {
            if (this.password == "" ) {
                token = client.connect();
                Log.d(TAG, "Connecting....");
            } else {
                token = client.connect(setupOptions());
            }
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    status = true;
                    sub();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure: " + exception.toString());

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void sendNewMessage(String data, String topic){
        try {
            MqttMessage m = new MqttMessage(data.getBytes("UTF-8"));
            m.setQos(2);
            client.publish(topic, m);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sub() {
        try {
            subTopics.forEach(topic ->{
                try {
                    client.subscribe(topic,0);
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            });

            client.subscribe("$SYS/broker/uptime", 0);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String strTopic = new String(topic);
                    if(strTopic.equals("$SYS/broker/uptime")){
                        String tempUptime = new String(message.getPayload());
                        tempUptime = tempUptime.substring(0, tempUptime.length() - 8);
                        try{
                            Double test = Double.parseDouble(tempUptime);
                            if(test > 0.0){
                                uptime = (test / 60);
                            }
                        }catch (Exception e){}
                        Log.d(TAG, tempUptime);
                    } else if(strTopic.equals("pico/btn")){
                        String msg = new String( message.getPayload() ) ;
                        adapter.add("T:" + topic + " Payload: " + msg);
                        dogData.addNow(Integer.parseInt(msg));
                        Log.d(TAG, "Btn press logged: " + Integer.parseInt(msg));

                    }else{
                        Log.d(TAG, "topic: " + topic);
                        Log.d(TAG, "message: " + new String(message.getPayload()));
                        adapter.add("T:" + topic + " Payload: "+ new String( message.getPayload()));
                    }

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

}
