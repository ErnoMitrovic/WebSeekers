package com.example.realclient;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Paho implements MqttCallback {
    private String topic, broker, clientID;
    private final int qos;
    private final MemoryPersistence persistence;
    private MqttClient client;

    public Paho() {
        topic = "";
        broker = "";
        clientID = "";
        qos = 2;
        persistence = new MemoryPersistence();
    }

    public Paho(String topic, String broker, String clientID) {
        this();
        this.topic = topic;
        this.broker = broker;
        this.clientID = clientID;
    }

    public void connect() throws MqttException {
        client = new MqttClient(broker, clientID, persistence);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        System.out.println("Connect to broker: " + broker);
        client.connect(options);
        System.out.println("Connected");
        client.setCallback(this);
    }

    public void sendMessage(String content) throws MqttException {
        MqttMessage message = new MqttMessage(content.getBytes(StandardCharsets.UTF_8));
        message.setQos(qos);
        client.publish(topic, message);
        System.out.println("Message published");
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            System.out.println("reason " + e.getReasonCode());
            System.out.println("msg " + e.getMessage());
            System.out.println("loc " + e.getLocalizedMessage());
            System.out.println("cause " + e.getCause());
            System.out.println("excep " + e);
            e.printStackTrace();
        }
        System.out.println("Disconnect");
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection Lost");
        cause.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Message arrived");
        System.out.println(topic + " " + message.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        try {
            System.out.println("Delivery complete");
            if (token.getMessage() != null){
                System.out.println(token.getMessage().toString());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
