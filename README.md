# Desktop Calendar Software for Multi-users

HKUST COMP3111 Spring 2015 Software Engineering. Professor Huamin Qu

Park Ji Ho(jihopark), Kim Jihyok(wotomas), Lim Sungsu(proflim), Ku Bon Kyung(angelmarine) (UST CSE Department UG)

  - Used libraries: Java Swing, Google Gson
  - Software Architecture imitating mediator model, Model-View-Controller Pattern of Rails.
  - (See our Project Visualization using Gource)[https://www.youtube.com/watch?v=UJAoBlTdVY0]
  ![Mediator Diagram](mediator_diagram.png)
  [See more here](Architecture Document.md)

##Software Requirements

###Phase I

The basic required features in the Phase I of the calendar system development is to implement the basic event scheduling functionality for a single user. It includes the following requirements.
  1. Single user environment: At this stage, you may assume that the calendar is used only by a single user to arrange her personal schedule. No other users are present or can view/access the information of the calendar.
  
  2. Basic event scheduling: The calendar must be able to provide basic scheduling capabilities for an individual user.
    - The user can schedule an event with the starting time, end time, the event description, the event location, the frequency, if a reminder is needed, how much time ahead the reminder should be triggered, and additional description for the event.
    - The time specification should be in intervals of 15 minutes. The event frequency can be one-time, daily, weekly, or monthly. Should a reminder be needed, it should be triggered and displayed to the user at or less than the specified time interval before the scheduled time of the event. The reminder should only be given ONCE.
    - The reminder information, the event location, and the event description can be optionally specified. Other information must be specified. The location must be selected from a list of predefined locations in the system.
    - The successfully scheduled event should cause GUI to change accordingly. An entry should show in the daily view and the day when the event is scheduled should change color on the month view.
  
  3. Location information: The location must be uniquely identified by its name. The user must select from a set of predefined locations. The locations can be added through a separate interface (Note that this interface does not exist in the current skeleton).
  
  4. Event validity: The user can neither schedule multiple events that overlap in time or in space nor an event happens in the past. The user can only modify or delete any events that have not yet happened.
  
  5. Notification: The calendar system should provide notification services to the user at a user-defined interval prior to the time when the event happens.
  
  6. Time machine: Do not program directly using your computer clock because, for testing purposes, we will ask you to fast forward and rewind the clock that is used by the calendar system. During the evaluation of the project, the evaluator will use the interface you provide, either on the GUI or from the console, to use this time travelling feature and test the functionalities of the implementation. You must make sure your entire calendar system is based on a changable clock.

###Phase II

The basic required features in the Phase II of the calendar system development is to implement the support of multiple users and use persistent storage for the calendar data. It includes the following requirements. 

  1. Registration: The system support multi-users, and two types of users, administrators and normal users. So a user must register by setting the username and password, as well as to indicate whether it's a admin or a normal user.

  2. Security: The system must protect one user's events from being viewed by others, unless these events are explicitly allowed to be publicly viewed. Therefore, the calendar system should implement a proper login functionality and should give the user a choice whether the event can be seen by other users at the time of scheduling.

  3. Group events: The system must support the scheduling of group events involving more than one users. All users must be pre-defined.
    - Two group events involving totally different participants can overlap in time but not in location.
    - A group event uses a group facility, it must satisfy the compacity constraint information.
    - A group event cannot be scheduled unless the time slot works for every participant.
    - A group event must go through a confirmation process that asks each participant to indicate the willingness of the attendance before the schedule is finalized. The event is not scheduled unless all participants have positively confirmed. If one participant negatively confirms or the confirmation process is not complete, the event should not be scheduled. The initiator should not need to confirm.
    - Only the event initiator can delete or modify the group event. No confirmation from participants is needed.

  4. Persistence: The system must support the safety of the data entered by storing the confirmed input to a permanent storage on the disk. Any valid information should be present when the calendar system is restarted.

  5. Maintenance: The calendar must support account management functionalities as follows.
    - The creation of two classes of users: the regular users and the administrative users.
    - Each class of the users have different privileges. Regular users can ONLY create and change her or his own credentials including the compulsory information such as the full name (first and last), the password, and the email address. Other information can also be specified. The administrator can inspect, change, and remove any users in the system.
    - Only the administrator can specify, change, or remove location information such as meeting rooms. If a location is a group facility (has a tag to indicate it), it must be given a readable name and capacity information.
    - Although the administrator have many privileges, she must apply a courtesy policy which states that, if a user or a location is to be removed, she must receive the confirmation of the initiators of all concerned events before finalizing the removal, and also send a notification to the user when he/she tries to login.
 
  6. Intelligence: The calendar supports a certain degree of computational intelligence to ease the scheduling of events. It should at least support two pieces of scheduling intelligence. First, after the initiator selects the participants and the date (may several days), it should provide a list of "schedulable" timeslots which are available for all the participants for the initiator to choose from. Second, it should be able to perform automatic event scheduling, especially for group events. All the participants can choose multiple available timeslots, it should locate the earliest timeslot that satisfies all the constraints from the participants.
