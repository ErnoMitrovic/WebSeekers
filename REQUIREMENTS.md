# 🗂️ SYSTEM REQUIREMENTS
+ ***CLIENT STAGE***  
Connection to users will be established via Android Devices, since they are popular devices and fulfill the connection requirements.  

+ ***GATEWAY / EDGE STAGE***  
The configuration of an edge gateway enables the transportation of Mosquitto Broker messages from devices as cellphones and our edge connector container. This will be the main channel in which we will receive the user’s status and maintain the constant connection to look up for any update.  

+	***CLOUD STAGE***  
Cloud stage will be approached in two ways:  

    - ***Python MYSQL x Mosquitto Broker Integration***
    
    The first one by implementing a python script in the Linux server through a PyCharm implementation, this script will establish the connection to our MQTT broker. As part of it’s functionality, it will display the connection status and keep sending messages with the user’s risk every 10 seconds.
    - ***Dashboard***  
    
    For the second part, it’s necessary to have a dashboard, which access will be provided to the operator’s members of the rescue team. This will be helpful to query the data received and identify it by colors according to the ‘risk level’, all this data will be displayed in a map with the exact latitude and longitude.
+ ***INFRASTRUCTURE***  
    
    To implement our system, we need to host it in a network to operate and receive the multiple requests. In this first stage, we will use ‘Mininet’ to simulate this network and verify the proper operation of each part of the system. Mininet is a network emulator which creates a network of virtual hosts, switches, controllers, and links. Mininet hosts run standard Linux network software.

# ⚙️ FUNCITONAL REQUIRMENTES
+ The system must allow users to introduce the brokers IP
+	The system must allow users to introduce the brokers port in usage 
+	The system must establish a connection to the network
+	The system must allow users to select their severity status
+	The system must send the data to the broker every 10 seconds

# 📱 EXTERNAL INTERFACE REQUIREMENTS
The software is exclusively created for Android devices, our system doesn’t demand a lot resources but we recommend to use Android devices from Android 7 Nougat and upwards, since they still have support from Google and can run every basic app without problems.

### Specific requirements:
+	Global Positioning System (GPS)  
Our app/system will need to have access to the GPS to send the exact device location to our broker.
+ A network connection  
It is required a connection to establish communication to the broker, Wi-Fi is the most popular way nowadays, but since the purpose of the project is highly probable that Wi-Fi service is not available, so a cellular connection will be more than enough (3G, 4G or 5G).

# 📡 NON-FUNCTIONALS REQUIREMENTS
+ When the users introduce the broker’s IP and Port, the “Connect” button will be enabled and after pressing it, the user will receive within 2 seconds a confirmation of connection
+ Once the connection is established, the user can select between three severity status (low, medium, high), when the “Update” button is pressed, a sent confirmation will appear within 3 seconds.
+ If the status of the user changes (either it improves or gets worst), a new severity status can be selected and then press the “Update”, an update confirmation will appear within 3 seconds.
