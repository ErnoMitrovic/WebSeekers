<!-- Improved compatibility of back to top link: See: https://github.com/ErnoMitrovic/WebSeekers/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the WebSeekers. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->

<p align="center"> <img src="https://github.com/ErnoMitrovic/WebSeekers/blob/main/fig/WS_logo.png" alt="WebSeekers Logo" width="65"></a></p>

<h1 align="center">GSL IoT Project: WebSeekers Team</h1>

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

<!-- ABOUT THE PROJECT -->
# 🗂️ HOW TO COMPILE <a name = "about-the-project"></a>
## ⚙️ Prerequisites
Adjust the requirements depending on the stage that you want to emulate.
<ul>
 <li>Have an android device or emulator</li>
 <li>Have mosquitto broker</li>
 
 ```
 $ sudo apt update 
 $ sudo apt install -y mosquitto
 $ sudo systemctl start mosquitto
 ```
 
 <li>Have mysql client</li>
 
 ~~~
 $ sudo apt-get update
 $ sudo apt-get mysql-client
 $ sudo apt install mysql-server
 $ sudo systemctl start mysql.service
 $ sudo mysql_secure_installation
 ~~~
 
 <li>Have grafana service</li>
 
 ```
 $ sudo apt-get install -y adduser libfontconfig1
 $ wget https://dl.grafana.com/enterprise/release/grafana-enterprise_9.3.1_amd64.deb
 $ sudo dpkg -i grafana-enterprise_9.3.1_amd64.deb
 $ sudo systemctl daemon-reload
 $ sudo systemctl start grafana-server
 ```
</ul>

To start a service as default on Ubuntu, do the following command `$ sudo systemctl enable {service_name}`

## :iphone: Client Stage
In order to get ready with the app, you must: 
 <ol>
  <li>Navigate through <a href="https://github.com/ErnoMitrovic/WebSeekers/releases" target="_blank"> releases </a></li>
  <li>Download the <a href="https://github.com/ErnoMitrovic/WebSeekers/releases/download/v1.0.0-beta/app-debug.apk" target="_blank"> .apk </a>into an android device</li>
</ol>

There the source code is also available for developers. It is recomended to develop it on Android Studio. 

## :vibration_mode: Gateway / Edge Stage
In order to start the broker, you must: 
 <ol>
  <li>Download the <a href="https://github.com/ErnoMitrovic/WebSeekers/releases/download/v1.0.0-beta/mosquitto_bridge.conf" target="_blank"> mosquitto bridge </a>configuration file</li>
 <li>Download the <a href="https://github.com/ErnoMitrovic/WebSeekers/releases/download/v1.0.0-beta/mosquitto_central.conf" target="_blank"> mosquitto central </a>configuration file </li>
  <li>Start the MQTT service by starting the mosquitto with the following commands: </li>
 
 ```
 mosquitto -c /etc/mosquitto/mosquitto_bridge.conf
 mosquitto -c /etc/mosquitto/mosquitto_central.conf
 ```
 
</ol>

## :cloud: Cloud Stage
To start as default, do the following command
`sudo systemctl enable mysql.service`

In order to start the broker, you must: 
 <ol>
  <li>Download the <a href="https://github.com/ErnoMitrovic/WebSeekers/tree/main/CloudStage" target="_blank"> Cloud Stage </a>folder</li>
 <li>Run Python and connect to the localhost</li>
 <li>Connect to the database named "rescues"</li>
 <li>Open Grafana</li>
</ol>
