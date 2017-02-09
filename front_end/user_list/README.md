###This application demostrate usage of front end MVC framework (backbone.js) and vanilla jQuery.


###Here are some features of the project:
1. The end-user can add/edit users to a page with the following fields:  
  * Last Name  
  * First Name  
  * Age  
  * Email  
  * Created On  
  * Last Edited  
  * Active  
2. The end-user can click on a row and begin inline editing of that row. Edit mode is turned on with the pencil icon. Edits can be confirmed with the circled green check icon or canceled with the circled red cross icon.  
3. The end-user is not able to save a record without data for the following fields  
  * Last Name  
  * First Name  
  * Email    
4. The end-user can delete users with the trash icon  
5. The end-user can turn a user active or inactive with the squared grey check icon or the squared green check icon in edit mode.   
6. The end-user can filter results by  
  * Last Name  
  * First Name  
  * Age  
  * Email  
  * Created On  
  * Last Edited  
7. The end-user cannot add duplicate users, where a duplicate user is defined as two people having the same email address  
8. Every column is sortable  
9. In non-filter mode, the screen shows 10 users at a time  
The project is written with both backbone and vanilla jquery because I was learning backbone and refreshing jquery so I wanted to practice both.   

To run this code, unzip the folder and double click on index.html. Or you can run index.html from Pycharm or Webstorm.  The application is pre-populated with 25 randomly generated users each time the page loads.   

The project should look like this on load:
Inline image 1
This is what a row looks like in edit mode:
Inline image 2

This is filter mode. Let's say we want all people named "Zach":
Inline image 3
