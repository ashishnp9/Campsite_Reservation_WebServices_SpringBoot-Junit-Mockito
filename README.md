# Campsite_Reservation_WebServices_SpringBoot-Junit-Mockito
Campsite_Reservation WebServices Application. This project is build in Spring boot 2 + Rest API + Junit and Mockito


mvn clean

mvn test

mvn clean install

Go to the target folder

java -jar Campsite_Reservation_App-0.0.1-SNAPSHOT.jar

Verify your RESTful call. URL : 

1) Get request for Availabe dates : 
   localhost:8080/campsite/dates

Response : 

    {
    "responseCode": 200,
    "responseMessage": "Availabe Dates successfully fetched..!",
    "errorDiscription": null,
    "httpHeaders": null,
    "responseObject": "[Nov 13] [Nov 14] [Nov 15] [Nov 16] [Nov 17] [Nov 18] [Nov 19] [Nov 20] [Nov 21] [Nov 22] [Nov 23] [Nov 24] [Nov 25] [Nov 26] [Nov 27] [Nov 28] [Nov 29] [Nov 30] [Dec 01] [Dec 02] [Dec 03] [Dec 04] [Dec 05] [Dec 06] [Dec 07] [Dec 08] [Dec 09] [Dec 10] [Dec 11] [Dec 12] ",
    "error": false
}


2) Post request for Resevation : 
   localhost:8080/campsite/reservation

Input Request :

{
	"name":"Ashish Patel",
	"emailAddress":"patel.ashish610@gmail.com",
	"startDate":"2018-11-13",
	"endDate":"2018-11-14"
}

Response : 

{
    "responseCode": 200,
    "responseMessage": "The Campsite reservation successfully created..! The ReservationId is :-> 4",
    "errorDiscription": null,
    "httpHeaders": {
        "Location": [
            "http://localhost:8080/campsite/reservation"
        ]
    },
    "responseObject": null,
    "error": false
}

3) Update request for Modify Reservation : 
   localhost:8080/campsite/reservation/3

    Input Request :

{
	"name":"ashish patel1234",
	"emailAddress":"ashish.patel@gmail.com",
	"startDate":"2018-11-15",
	"endDate":"2018-11-16"
}

Response : 

{
    "responseCode": 200,
    "responseMessage": "Your reservation has been modified successfully..! ",
    "errorDiscription": null,
    "httpHeaders": {
        "Location": [
            "http://localhost:8080/campsite/reservation/3"
        ]
    },
    "responseObject": null,
    "error": false
}

4) Delete request for Delete Reservation : 
   localhost:8080/campsite/reservation/3
