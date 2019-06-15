This is a Location based Message Board Application with a database included called “Firebase”. Firebase is a database that can store the information about a user and we can also retriev information from Firebase. 
On starting page, it asks for a username, and keeps that username until the comment feed. If the username is empty or user leaves the field blank, then it will alert the user by popping a message “Enter a valid username”. Once username is put, user clicks on the start bears button which takes them to different landmarks of bears page where it gives you the distance from your current location to all given bears. If the distance from your current location to a bear is less than 10 meters, then the text will appear in green color indicating you can click that image and post a comment. If the distance is longer than 10 meters then you will not be able to post a comment and an alert message will be displayed saying, “You must be within 10 meters of the landmarks’ location to access comment feed”. 
Once clicked a reachable image, the app opens a new comment feed where user can actually post a comment. Also, at this point, in the comment feed the app will retrieve all the previous comments in order they were posted, from the Firebase and display the username, the date and the comment they posted. 

After posting a comment, it will say username and message that user had posted along with current time and date.
Essentially, the application is using three views, and different activities to jump around views. This includes part 1 of the application and it fulfills the requirements for implementing Firebase. 

Link to the Screencast: https://youtu.be/7VY0LOcoJLQ


Written and developed by:
Gurpreet Singh
