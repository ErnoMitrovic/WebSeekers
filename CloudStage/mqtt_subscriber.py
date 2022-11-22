'''
To install paho-mqtt run this command in the terminal:  pip install paho-mqtt
A nice tutorial is here: https://pypi.org/project/paho-mqtt/
'''
import random

import mysql_utils as db_manager

from paho.mqtt import client as mqtt_client

broker = '20.249.56.79'
port = 1884
topic = "srv/temperature"
client_id = f'python-mqtt-{random.randint(0, 100)}'
username = 's2'
password = 's2987654321'

def connect_mqtt() -> mqtt_client:
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)

    client = mqtt_client.Client(client_id)
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port)
    return client

def subscribe(client: mqtt_client):
    def on_message(client, userdata, msg):
        print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")
    client.subscribe(topic)
    client.on_message = on_message

def run():
    client = connect_mqtt()
    subscribe(client)
    client.loop_forever()

run()