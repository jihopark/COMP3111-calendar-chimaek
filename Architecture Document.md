# Mediator Pattern

## Motivation

The calendar project requires numerous GUI components and data components to communicate simultaneously and complex integration due to many relationships among different data objects. 
For this reason, if handling of data is implemented on the GUI side, it is impossible track down where data components are modified or created.
Also, if integration among different data objects are all done on the unit side, it is not possible to handle many data objects at the same time. 

## Solution

We imitated the Model-View-Controller(MVC) architecture from the web application platform, Ruby on Rails, to mediate the model(data object) and view(GUI component) with having controllers.
Controllers, each of which hold data objects like Appt, User, Notification, Invitation, and Location, are implemented in singleton pattern, so that GUI components easily communicate with data objects through them.
Integrations among different data objects, such as Appt and Notification, or User and Appt, are handled through different methods of controllers. 
As a result, each controller has one-to-many relationship with data objects, which makes the code maintenance simpler with centralized control logic.

## Class Diagram

![Mediator Diagram](mediator_diagram.png)


# Singleton Pattern

## Motivation

In the calendar project, Model-View-Controller(MVC) architecture was employed and therefore, most of the data manipulating process rely on the controller units, for example, ApptController, UserController, LocationController and so on.
Therefore, if there is any data to be manipulated, a controller object would be needed. Without Singleton Pattern, for every data manipulation, a controller object would be instantiated.
If the calendar is utilized by many users, massive amount of data should be manipulated and hence, the number of controller objects will grow uncontrollably.
In the current system, all the data of a specific type (e.g appointment) is stored in ONE respective storage unit (e.g appointment storage unit). 
And this implies that all the controller objects of the same type should refer to ONE respective storage unit.
Therefore, a single specific-type controller object with reference to its respective storage unit is sufficient to handle all the data manipulation of its type, instead of unneccesarily creating a controller object everytime whenever data manipulation is required.
Furthermore, creating many controller objects would be inefficient because once a controller object is created and used for a data manipulation process, it would not be used again for another data manipulation (since a new controller object is created for another data manipulation) and this will eventually  
lead to waste of resources. If too many objects are created in this way, this might even cause the program to crash.

## Solution  

We implemented the controller units of the different types of data in Singleton Pattern, which we learnt from the lecture, to ensure that only ONE object is instantiated for each controller unit classes.
As a consequence of using Singleton Pattern, there was no waste of memory resources since it prevented creating controller objects unneccesarily. 
Manipulation of data of a specific type is done by referring to the Singleton object of the controller of its type. 
For example, when creating an Appt, a method *createNewAppt()* is called from the Singleton object of the ApptController. 
Singleton Pattern not only improved efficiency and stability of the program but also enhanced the code style and maintenability, due to the fact that no extra lines of instantiation of controller objects were required and all data manipulation referred to one single object.

## Class Diagram

![Singleton Diagram](singleton_diagram.png)
