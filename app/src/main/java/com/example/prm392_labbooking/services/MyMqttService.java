package com.example.prm392_labbooking.services;

import android.util.Log;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MyMqttService {
    private static final String TAG = "MqttService";
    private static final String BROKER_URL = "tcp://broker.emqx.io:1883";
    private static final String CLIENT_ID = "PRM392LabBookingClient";
    private MqttClient client;

    public void initialize() {
        try {
            client = new MqttClient(BROKER_URL, CLIENT_ID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
            Log.i(TAG, "Connected to MQTT broker at " + BROKER_URL);
        } catch (MqttException e) {
            Log.e(TAG, "Failed to connect to MQTT broker: " + e.getMessage(), e);
        }
    }

    public void sendHelloWorld(String topic) {
        try {
            if (client != null && client.isConnected()) {
                MqttMessage message = new MqttMessage("Hello, World!".getBytes());
                client.publish(topic, message);
                Log.i(TAG, "Message published to topic " + topic);
            } else {
                Log.e(TAG, "Client is not connected. Cannot send message.");
            }
        } catch (MqttException e) {
            Log.e(TAG, "Failed to publish message: " + e.getMessage(), e);
        }
    }

    public void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                Log.i(TAG, "Disconnected from MQTT broker.");
            }
        } catch (MqttException e) {
            Log.e(TAG, "Failed to disconnect: " + e.getMessage(), e);
        }
    }

    public void testService() {
        initialize();
        sendHelloWorld("test/topic/labbooking");
        disconnect();
    }

    public void subscribe(String topic, IMqttMessageListener listener) throws MqttException {
        if (client == null || !client.isConnected()) {
            initialize();
        }
        client.subscribe(topic, listener);
        Log.i(TAG, "Subscribed to topic: " + topic);
    }

    public void publish(String topic, String message) throws MqttException {
        if (client == null || !client.isConnected()) {
            initialize();
        }
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        client.publish(topic, mqttMessage);
        Log.i(TAG, "Published message to topic: " + topic);
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }
}
