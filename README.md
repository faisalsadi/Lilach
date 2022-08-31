# Lilach
SW Engineering Project, 2021-2022, University of Haifa . 


## Structure
Pay attention to the three modules:
1. **client** - a simple client built using JavaFX and OCSF. We use EventBus (which implements the mediator pattern) in order to pass events between classes (in this case: between SimpleClient and PrimaryController.
2. **server** - a simple server built using OCSF.
3. **entities** - a shared module where all the entities of the project live.

## Running
1. Run Maven install **in the parent project**.
2. Run the server using the exec:java goal in the server module.
3. Run the client using the javafx:run goal in the client module.
4. Press the button and see what happens!

![Screenshot 2022-08-31 115718](https://user-images.githubusercontent.com/31912809/187640857-a4d93d34-db5a-4fd3-be87-8aa62bf2405f.jpg)
![Screenshot 2022-08-31 115651](https://user-images.githubusercontent.com/31912809/187640871-cf0c762a-4741-45a1-8c31-f195ebf49db1.jpg)
![Screenshot 2022-08-31 115921](https://user-images.githubusercontent.com/31912809/187640875-7ad31f67-86d6-4f71-8ad2-afece6d8657e.jpg)
