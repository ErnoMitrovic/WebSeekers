<!-- Improved compatibility of back to top link: See: https://github.com/ErnoMitrovic/WebSeekers/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the WebSeekers. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->

# GSL IoT Project

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]


<!-- TABLE OF CONTENTS -->
# 📝 Table of Contents
<ol>
  <li><a href="#about-the-project"> About the project </a></li>
  <li><a href="https://github.com/ErnoMitrovic/WebSeekers/blob/main/USER_STORIES.md" target="_blank"> User Stories </a></li>
  <li><a href="#planning"> Planning </a></li>
  <li><a href="#collaborators"> Collaborators </a></li>
  <li><a href="https://github.com/users/ErnoMitrovic/projects/1"> Project </li>
</ol>

<!-- ABOUT THE PROJECT -->
# 🗂️ About The Project <a name = "about-the-project"></a>

## Collaboration
Global Shared Learning: Classroom is an initiative that aims to link a course at the Tecnológico de Monterrey with a course at an associated international university, in this case the Instituto Tecnológico de Aeronáutica (ITA), through an international element, using technological tools to connect with others students, foster collaboration and facilitate learning in multicultural environments.
Therefore, with the help of the international component, this course aims to demonstrate the main IoT foundations through the implementation of a digital system prototype that obtains data through sensors, processes it, and presents it on an Internet platform for later analysis and visualization.

## Scenario

A disaster is a sudden and unfortunate event that brings with it a great damage, loss, and destruction of local infrastructure. The WHO has defined it as "any sudden occurrence of events that cause damage, ecological disruption, loss of human life, deterioration of health and health services, on a scale sufficient to waeeant an extraordinary response from outside the affected community or area". 

<p align="center" width="100%"><a name = "#itajai" href="https://youtu.be/9WIwlljva_s"><img src="https://i.ytimg.com/an_webp/9WIwlljva_s/mqdefault_6s.webp?du=3000&sqp=CNr8yJsG&rs=AOn4CLAJ00MSjS1BSyKD8LfwL9qLbrbPnw" alt="Disaster Management. Disasters: types and effects" width="65%"></a></p>

Consequently, disaster management is extremely important to suriveve in the case of a major unfortunate evenet (natural or man-made). Therefore, this important concept can ve defined as the organisation aspects of emergencies through the application of response and recovery in order to lessen the impact.
For instance, if the local infrastructure is affected and there are no telecommunication resources, with a good disaster management, the essential communication resources coulde be provided in the response stage in order to supply a complete telecommunication infrastructure that provides Command and Control to support the operations.

## Solution

Considering the importance that disaster management has in both response and recovery stages, the objective of this project is to provide an environment to send distress messages when a disaster occurs. Consequently, it is important to consider that for this scenario the local infrastructure has been affected and there are no telecommunication resources to provide communication to crictical people or groups, such as rescue teams. Furthermore, it is worth mentioning that because this project is focused on the response stage, it considers the usage of the menas existent in the community, the help of volunteers and technology such as drones to provide telecommunication resources that hel support the identification of hazards. 

For instance, in general, the victims of a disaster that have access to their smartphone will have an app which enables them to constantly send their position. Consequently, if they are in danger, which can be measured by a severity level (high, medium or low), through a basic text message with a description of their current situation a drone flying over the affected are could ingercept the message and be able to forward it to other drones until the message arrives at the CMC. Finally, the CMC data would be processed and aggregated in order to provide a dashboard for the operators of emergency services in order to deploy an eficcient rescue operation.

<p align="center" width="100%"><a name = "#arch"><img src="https://www.gravoc.com/wp-content/uploads/2018/01/Drones-to-the-Rescue-How-Drones-Are-Helping-Disaster-Relief-Efforts-1080x343.png" width="65%"></a></p>


Using Iot concepts, the solution for this challenge is the following: there will be two groups of nodes, emulated and real ones. In this case, the user nodes will represent the sensor and the actuator stage. Likewise, regarding the real node, it will be a device which periodically sends MQTT messages to the application server through bridge brokers (Mosquitto) that exist in the emolution and that inform the severity level and position of a victim, or user. On the other hand, the emulated nodes are Python scripts that have the same functionality as the real ones. 

Thereupon, an application server will provide the features to the Command-and-Control (C2) users. Consequently, the real node is the one that will run on a server box, in this case Linux. Therefore, a virtual interface will be used in the emulation environemnt to enable the real and emulated nodes to exchange messages.


Moreover, it is worth mentioning that the server will have a Mosquitto borker, which will receive all the messages from the bridge ones and will store them in a SQL database, Finally, a dashboard will be implemented in order to provide a better visualization, by presenting the information, a map and an estimate of the Risk KPI.


<p align="center" width="100%"><a name = "#detarch"><img src="https://connectivity-staging.s3.us-east-2.amazonaws.com/s3fs-public/inline-images/Node-Gateway-Cloud_Network.png" alt="Detailed Architecture" width="65%"></a></p>

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- GETTING STARTED -->
# 👥 User stories <a name = "user-stories"></a>

>As an user of the application I want the ability to send the SOS message even if I didn't have local internet connection. 
>- End user goal: Send SOS message.
>- End business goal: Track where users are in a disaster scenario.
>- Acceptance criteria: Send Message, User receives confirmation, Message is sent to the cloud, Admin receives the information on the Dashboard.
>- Measurement of success: Admin receive the location of the user in an inhospitable scenario.

>As an admin of the application I want to send confirmation messages to the users through the network. 
>- End user goal: Send confirmation message.
>- End business goal: Notify users that their message was received.
>- Acceptance criteria: Admin sends confirmation, cloud send to the mininet, user receives confirmation.
>- Measurement of success: User receives message confirmation in an inhospitable scenario

>As a helping organization I want to recieve SOS messages to operate quickly.
>- End user goal: recieve SOS messages.
>- End bussiness goal: operate quickly during a disaster scenario.
>- Acceptance criteria: Request SOS message, Server recieves the query, Drones interconnect to send the message, Message is send to the organization.
>- Measurement of success: Organization recieves the location of the user in an inhospitable scenario.


# 📝 Planning <a name = "planning"></a>
## Sprint planning
All sprints are one week long. Sprint planning takes place at the beginning of the week.
## Sprint review and retro
Sprint review and sprint retro occur at the end of each week when deliverables are sent to professors.

# 🤝🏻 Collaborators <a name = "collaborators"></a>
### ITESM (Mexico) & ITA (Brazil)
- Caio Graca Gomes              - (ITA)
- Ernesto Miranda Solís         - (TEC)
- Henrique Fernandes Feitosa    - (ITA)
- José Andrés Rodríguez Ruiz    - (TEC)
- Juan Antonio Mancera Velasco  - (TEC)
- Owen Rosales Castro           - (TEC)
- Tania Regina Ramírez Vázquez  - (TEC)
- Thomas Neto                   - (ITA)
- Victoria Rodríguez de León    - (TEC)

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/ErnoMitrovic/WebSeekers.svg?style=for-the-badge
[contributors-url]: https://github.com/ErnoMitrovic/WebSeekers/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/ErnoMitrovic/WebSeekers.svg?style=for-the-badge
[forks-url]: https://github.com/ErnoMitrovic/WebSeekers/network/members
[stars-shield]: https://img.shields.io/github/stars/ErnoMitrovic/WebSeekers.svg?style=for-the-badge
[stars-url]: https://github.com/ErnoMitrovic/WebSeekers/stargazers
[issues-shield]: https://img.shields.io/github/issues/ErnoMitrovic/WebSeekers.svg?style=for-the-badge
[issues-url]: https://github.com/ErnoMitrovic/WebSeekers/issues
[license-shield]: https://img.shields.io/github/license/ErnoMitrovic/WebSeekers.svg?style=for-the-badge
[license-url]: https://github.com/ErnoMitrovic/WebSeekers/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/ErnoMitrovic
[product-screenshot]: images/screenshot.png
[Next.js]: https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Vue.js]: https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D
[Vue-url]: https://vuejs.org/
[Angular.io]: https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white
[Angular-url]: https://angular.io/
[Svelte.dev]: https://img.shields.io/badge/Svelte-4A4A55?style=for-the-badge&logo=svelte&logoColor=FF3E00
[Svelte-url]: https://svelte.dev/
[Laravel.com]: https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white
[Laravel-url]: https://laravel.com
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[JQuery.com]: https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white
[JQuery-url]: https://jquery.com 
