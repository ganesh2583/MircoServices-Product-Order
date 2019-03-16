# MircoServices-Product-Order
This project creates two Micro Services which interact with each other using Google Pub Sub mechanism for Async communication and REST weservices call for Sync communication. Hystrix is configured as Curcuit breaker for Sync REST Communication incase there is an outage of the service.

There are two services in this project:
  - Product Service
  - Order Service.
  
Product service is responsible for identifying the stock quantity for a given product. 
Order service is responsible for placing an order. 
  
Once an order request is received by Order service, it responds asynchronysly to user and triggers and "OrderPlaced:<productname>" event into the Google Pub Sub queue. 
This event is subscribed by Product Service and is acknowledged by positive or negetive response based on the Inventory count like OutOfStock or InStock. 
This above generated events can be subscribed by Order Service to take further actions. 
  
**Usage**

Below values in the application.properites can be modified for both the projects with the Google Cloud account information 
```
google.cloud.projectId=exitvibe
google.cloud.productservice.topicId=team7
google.cloud.productservice.subscriberId=team7
google.cloud.orderservice.topicId=team7
google.cloud.orderservice.subscriberId=team7
```

 
Projects can be started with command:
 ```
 gradle bootRun
 ```
