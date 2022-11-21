package com.example.realclient;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;

/**
 * <h1>Paho</h1>
 * <p>The {@code Paho} class implements the functionality to operate a MQTT protocol
 * using Paho from Eclipse.</p>
 * @see org.eclipse.paho.client.mqttv3.MqttCallback
 * @author ErnoMitrovic
 * @version 1.0
 * @since 2022-11-21
 * */
public class Paho implements MqttCallback {
    private String topic, broker, clientID, messageReceived;
    private final int qos;
    private final MemoryPersistence persistence;
    private MqttClient client;

    /**
     * Default constructor for class Paho.
     * */
    public Paho() {
        topic = "";
        broker = "";
        clientID = "";
        qos = 2;
        persistence = new MemoryPersistence();
        messageReceived = "";
    }
    /**
     * Initialize a Paho instance with its topic, broker and clientID
     * @param topic topic to subscribe.
     * @param broker direction of the broker.
     * @param clientID id of the client (unique id).
     * */
    public Paho(String topic, String broker, String clientID) {
        this();
        this.topic = topic;
        this.broker = broker;
        this.clientID = clientID;
    }

    /**
     * Stablishes a connection with the broker
     * @throws MqttException if it can not establish connection.
     * */
    public void connect() throws MqttException {
        client = new MqttClient(broker, clientID, persistence);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        System.out.println("Connect to broker: " + broker);
        client.connect(options);
        System.out.println("Connected");
        client.setCallback(this);
    }
    /**
     * Sends message to the broker with the initialized topic.
     * @param content message tu send
     * @throws MqttException if message could not be send correctly.
     * */
    public void sendMessage(String content) throws MqttException {
        MqttMessage message = new MqttMessage(content.getBytes(StandardCharsets.UTF_8));
        message.setQos(qos);
        client.publish(topic, message);
        System.out.println("Message published");
    }
    /**
     * Disconnects from server, manages excpetions internally.
     * */
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
    /**
     * Callback for the lost connection.
     * @param cause possible cause for interruption.
     * */
    @Override
    public void connectionLost(Throwable cause) {
        System.err.println("Connection Lost");
        cause.printStackTrace();
    }
    /**
     * Callback if message arrived from server.
     * @param topic topic though which the message was send.
     * @param message MqttMessage arrived.
     * @see MqttMessage
     * */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Message arrived");
        System.out.println(topic + " " + message.toString());
        messageReceived = message.toString();
    }

    /**
     * Callback for complete delivery
     * @param token the delivery token of the transaction.
     * */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        try {
            System.out.println("Delivery complete");
            if (token.getMessage() != null) {
                System.out.println(token.getMessage().toString());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    /**
     * If message was received from broker.
     * @return the string representation of the received message
     * */
    public String getMessageReceived() {
        return messageReceived;
    }

    /**
     * Check the connection status
     * @return true if connected, false otherwise.
     * */
    public boolean checkConnection() {
        return client.isConnected();
    }
}
