# 👥 User stories <a name = "user-stories"></a>

>As an user of the application I want the ability to send the SOS message even if I didn't have local internet connection. 
>- End user goal: Send SOS message.
>- End business goal: Track where users are in a disaster scenario.
>- Acceptance criteria: Send Message, User receives confirmation, Message is sent to the cloud, Admin receives the information on the Dashboard.
>- Measurement of success: Admin receive the location of the user in an inhospitable scenario.

<br/>
<table>
<tr> 
  <th colspan="3" style="border: 1px solid #dddddd; padding: 8px;"> ACCEPTANCE CRITERIA </th>
</tr>
<tr> 
  <th> Scenario </th>
  <th colspan="3" style="border: 1px solid #dddddd; padding: 8px;"> SEND A SOS MESSAGE THROUGH A MOBILE DEVICE </th>
</tr>
<tr>
  <td> Given </td>
  <td> The users have the application installed </td>
</tr>
<tr>
  <td> And </td>
  <td> Have either a Wi-Fi or cellular connection </td>
</tr>
<tr>
  <td> Then </td>
  <td> Send the rescue message with the risk level </td>
</tr>
<tr>
  <td> And </td>
  <td> Update the risk if it’s needed </td>
</tr>
</table>

>As an admin of the application I want to send confirmation messages to the users through the network. 
>- End user goal: Send confirmation message.
>- End business goal: Notify users that their message was received.
>- Acceptance criteria: Admin sends confirmation, cloud send to the mininet, user receives confirmation.
>- Measurement of success: User receives message confirmation in an inhospitable scenario

<br/>
<table>
<tr> 
  <th colspan="3" style="border: 1px solid #dddddd; padding: 8px;"> ACCEPTANCE CRITERIA </th>
</tr>
<tr> 
  <th> Scenario </th>
  <th colspan="3" style="border: 1px solid #dddddd; padding: 8px;"> RECEIVE MESSAGES AND SEND RESCUE TEAMS </th>
</tr>
<tr>
  <td> Given </td>
  <td> The operators have access to the dashboard </td>
</tr>
<tr>
  <td> Then </td>
  <td> They request the data </td>
</tr>
<tr>
  <td> Then </td>
  <td> Visualize it by risk level according to the color and position </td>
</tr>
<tr>
  <td> When </td>
  <td> The targets are mapped </td>
</tr>
<tr>
  <td> And </td>
  <td> Are divided by areas </td>
</tr>
<tr>
  <td> Then </td>
  <td> Send rescue teams to each area </td>
</tr>
</table>

>As a helping organization I want to recieve SOS messages to operate quickly.
>- End user goal: recieve SOS messages.
>- End bussiness goal: operate quickly during a disaster scenario.
>- Acceptance criteria: Request SOS message, Server recieves the query, Drones interconnect to send the message, Message is send to the organization.
>- Measurement of success: Organization recieves the location of the user in an inhospitable scenario.

<br/>
<table>
<tr> 
  <th colspan="3" style="border: 1px solid #dddddd; padding: 8px;"> ACCEPTANCE CRITERIA </th>
</tr>
<tr> 
  <th> Scenario </th>
  <th colspan="3" style="border: 1px solid #dddddd; padding: 8px;"> SEND THE DATA TO THE RESCUE TEAMS </th>
</tr>
<tr>
  <td> Given </td>
  <td> The operators have access to the dashboard </td>
</tr>
<tr>
  <td> And </td>
  <td> Have direct contact with the rescue team </td>
</tr>
<tr>
  <td> Then </td>
  <td> Order the data according to the affected zones </td>
</tr>
<tr>
  <td> Then </td>
  <td>  Send the location and user data according to the zone </td>
</tr>
<tr>
  <td> Then </td>
  <td> Rescue teams can help groups of people within a certain range, in this way they can accomplish the goal of saving the most people possible </td>
</tr>
</table>
