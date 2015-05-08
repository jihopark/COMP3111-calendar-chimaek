Weekly Appointment Data Visualization System

We have implemented a data visualization system to show each user's pattern of schedule according to weekday.
The frequency of appointments is shown in a form of circle - thus the radius of the circle is proportional to the number of appointments.
By this feature, we can see which day the user is busy and can compare among users. This will help user to see which day is overloaded and distribute his appointments accordingly.

We used Graphic 2D library in Java Swing to implement this feature.  

We have brainstormed the idea and architecture together, and the implementation is done by Jihyok Kim.

The Weekly Appointment Data Visualization System consists of two parts: the CircleComponent.java file which extends JPanel and WeeklyDataVisualizationDialog.java which extends JFrame.
When the WeeklyDataVisualizationDialog is initialized, it initially creates base code of JLabeles to label the days of a week. Then using for-loops, it creates lines of circles that represent each user's appointment ratio accordingly.
When each user is called from the UserController, this user is redirected into the AppointmentController to retrieve the list of Appointments. These appointments go through another for-loop to calculate how many of the appointments are from which day of the week.
Once all these integers are calculated, the numbers go through another loop to calculate the radius of each circles reflecting the ratio of the numbers of the entire appointment each user has. Finally, WeeklyDataVisualizationDialog creates individual CircleComponents and visualizes it.

Users can access this bonus feature by pressing the 'Bonus' button under the Appointment tab.

Check BonusGUI.png we have attached.