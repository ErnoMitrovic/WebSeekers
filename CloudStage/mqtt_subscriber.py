'''
To install paho-mqtt run this command in the terminal:  pip install paho-mqtt
A nice tutorial is here: https://pypi.org/project/paho-mqtt/
'''
import random
from getpass import getpass
import socket
from pojos import Users, Risks, Locations, SeverityCount, RiskCount

import mysql.connector
import datetime

import mysql_utils as db_manager

from paho.mqtt import client as mqtt_client

broker = socket.gethostbyname(socket.gethostname())
port = 1884
topic = "rescues/severity"
client_id = f'python-mqtt-{random.randint(0, 100)}'
username = 's2'
password = 's2987654321'


def connect_db() -> mysql.connector.connection:
    user_db = input("Write username: ")
    pass_db = getpass()
    db_name = "rescues"
    return db_manager.open_connection(db_name, user_db, pass_db)


def update_user(connection: mysql.connector.connection, user_id: str):
    cursor = connection.cursor()
    query = "SELECT EXISTS(SELECT * FROM users WHERE user_id=\"{}\")".format(user_id)
    cursor.execute(query)
    cond = cursor.fetchone()
    cursor.execute("SELECT severity FROM risks WHERE user_id=\"{}\"".format(user_id))
    severity_saved: str = cursor.fetchone()
    user = Users.User(user_id, datetime.datetime.now().strftime("%H:%M:%S"))
    if bool(cond[0]):
        db_manager.update_values(connection, "users", user, f'user_id=\"{user_id}\"')
    else:
        db_manager.add_values(connection, "users", user)
    cursor.close()


def update_severity(connection: mysql.connector.connection, user_id: str, severity):
    cursor = connection.cursor()
    cursor.execute("SELECT severity FROM risks WHERE user_id=\"{}\"".format(user_id))
    severity_saved: str = cursor.fetchone()
    if severity_saved[0] != severity:
        update_user(connection, user_id)
    cursor.execute("SELECT last_update FROM users WHERE user_id=\"{}\"".format(user_id))
    last_update = cursor.fetchone()
    risk_obj = Risks.Risk(user_id, severity, "")
    delta = datetime.datetime.now() - last_update[0]
    if severity_saved == "medium" and severity == "medium":
        if delta.minute >= 20:
            risk_obj.risk = "high"
        else:
            risk_obj.risk = "medium"
    else:
        risk_obj.risk = severity
    query = "SELECT EXISTS(SELECT * FROM risks WHERE user_id=\"{}\")".format(user_id)
    cursor.execute(query)
    exists = cursor.fetchone()
    if bool(exists[0]):
        db_manager.update_values(connection, "risks", risk_obj, f'user_id=\"{user_id}\"')
    else:
        db_manager.add_values(connection, "risks", risk_obj)
    cursor.close()


def update_counts(connection: mysql.connector.connection):
    types = ["Low", "Medium", "High"]
    cursor = connection.cursor()
    for sev in types:
        query = f' INSERT INTO sev_count SELECT "{sev}", (SELECT COUNT(severity) FROM risks WHERE severity="{sev}") WHERE ' \
                f'NOT EXISTS (SELECT * FROM sev_count WHERE sev_type = "{sev}") ;'
        cursor.execute(query)
    connection.commit()
    for risk in types:
        query = f' INSERT INTO risk_count SELECT "{risk}", (SELECT COUNT(risk) FROM risks WHERE risk="{risk}") WHERE ' \
                f'NOT EXISTS (SELECT * FROM risk_count WHERE risk_type = "{risk}") ;'
        cursor.execute(query)
    connection.commit()
    for sev in types:
        query = f'UPDATE sev_count SET sev_c=(SELECT COUNT(severity) FROM risks WHERE severity="{sev}")' \
                f' WHERE sev_type="{sev}"'
        cursor.execute(query)
    connection.commit()
    for risk in types:
        query = f'UPDATE risk_count SET risk_c=(SELECT COUNT(risk) FROM risks WHERE risk="{risk}") ' \
                f'WHERE risk_type="{risk}"'
        cursor.execute(query)
    connection.commit()
    cursor.close()


def update_location(connection: mysql.connector.connection, user_id, lat, lon):
    cursor = connection.cursor()
    query = "SELECT EXISTS(SELECT * FROM locations WHERE user_id=\"{}\")".format(user_id)
    cursor.execute(query)
    exists = cursor.fetchone()
    location = Locations.Location(user_id, lat, lon)
    if bool(exists[0]):
        db_manager.update_values(connection, "locations", location, f'user_id=\"{user_id}\"')
    else:
        db_manager.add_values(connection, "locations", location)


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
        # user_id:severity:location
        message = str(msg.payload.decode("utf-8")).split(sep=':')
        location = message[2].split(sep=',')
        print("Message", message)
        print("Location", location)
        update_severity(connection, message[0], message[1])
        update_location(connection, message[0], location[0], location[1])
        update_counts(connection)

    client.subscribe(topic)
    client.on_message = on_message


def run(connection):
    client = connect_mqtt()
    subscribe(client, connection)
    client.loop_forever()

if __name__ == "__main__":
    connection = connect_db()
    db_manager.start_values(connection)
    run(connection)
