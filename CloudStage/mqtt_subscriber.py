'''
To install paho-mqtt run this command in the terminal:  pip install paho-mqtt
A nice tutorial is here: https://pypi.org/project/paho-mqtt/
'''
import random
from getpass import getpass

import mysql.connector

import mysql_utils as db_manager

from paho.mqtt import client as mqtt_client

broker = '20.196.203.144'
port = 1884
topic = "rescues/severity"
client_id = f'python-mqtt-{random.randint(0, 100)}'
username = 's2'
password = 's2987654321'


def connect_db() -> mysql.connector.connection:
    user_db = input("Write username: ")
    pass_db = getpass()
    db_name = input("Database name: ")
    return db_manager.open_connection(db_name, user_db, pass_db)


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


def subscribe(client: mqtt_client, connection: mysql.connector.connection):
    def on_message(client, userdata, msg):
        print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")

    client.subscribe(topic)
    client.on_message = on_message


def run():
    client = connect_mqtt()
    connection = connect_db()
    subscribe(client, connection)
    client.loop_forever()


run()
