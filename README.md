# Jumbo

###  Overview
This project aims to provide API to load the nearest locations based on the specific coordinate. there is a Stores.json file (in the map module) that all stores location are 
available there, so after the application started successfully it will try to persist them in a database (MongoDB) and then there are APIs (in the customer-panel) that offer
general search mechanism (RSQL) alongside near-query.

###  Module Definition
There are two modules in this project:
* Customer-panel : It will act as a panel for customers and API exposed here, it has an aggregating charisma inside it that will call any required resources to answer the client, so it will call the map module to load the nearest stores
* Map: This module serves under a Map domain in the project and will handle all the related map business, it will directly work with the database and expose methods to consume.

Customer-panel is an executive module and the Map module will be used as a dependency in Customer-panel and it has the potential to act solo in the feature under this domain.

###  Technologies
* Java 11
* SpringBoot 2.4.3
* Gradle 6.8.3
* MongoDB 4.4.14
* JIB 3.1.4
* TestContainers-MongoDB 1.17.2
* Swagger-UI 3.0.0
* Rest-Query-Engine 0.7.1


### API Structure

There are two search API with similar structures but the difference in the data dosage return and processing approach.

```
http://{{host-name}}:{{mapped-port}}/customer-panel/stores/search
http://{{host-name}}:{{mapped-port}}/customer-panel/stores/detailed-info/search
```
Explain API query params
* longitude -> should not be empty
* latitude -> should not be empty
* pageSize -> number of elements that appeared on the page, value is between 1 to 100
* pageNumber ->  page number of the returned items and starts from zero to 10000
* orderByProperties -> name of the properties that order by occurred and separate properties by a comma. for instance: name, code
* direction -> if order properties are defined, this field indicates the direction of order. it supports two types: ASC or DESC
* search -> this field accepts the RSQL mechanism and adds criteria alongside near query


Sample API response in detailed-info search
```json
{
                "id": "XNsKYx4XolwAAAFN9CQ7frI2",
                "longitude": 5.608147,
                "latitude": 52.336666,
                "locationType": "SupermarktPuP",
                "todayOpen": "08:00",
                "todayClose": "21:00",
                "city": "Harderwijk",
                "postalCode": "3844 HR",
                "street": "Achterste Wei",
                "street2": "3",
                "street3": "",
                "addressName": "Jumbo Harderwijk Stadsweiden",
                "complexNumber": "33535",
                "showWarningMessage": true,
                "collectionPoint": true,
                "sapStoreID": "3738"
            }
```
Sample API response in regular search
```json
{
                "id": "yc0KYx4XnCoAAAFIg94YwKxJ",
                "longitude": 3.548912,
                "latitude": 51.395932,
                "locationType": "SupermarktPuP",
                "todayOpen": "08:00",
                "todayClose": "20:00"
            }
```



API request examples
```
/search?longitude=3.911&latitude=53.33&pageSize=5&pageNumber=0
-> perform near query and return the first 5 result
```
```
/search?longitude=3.911&latitude=53.33&pageSize=5&pageNumber=1 
-> perform near query and return the second 5 result
```
```
/search?longitude=3.911&latitude=53.33 
-> perform near query and return the 20 first result, because default pageNumber is 0 and pageSize is 20
```
```
/search?longitude=3.911&latitude=53.33&direction=ASC&orderByProperties=locationType&pageSize=5&pageNumber=0 
-> perform near query and return the first 5 result and order by them based on location type in ascending format
```
```
/search?longitude=3.911&latitude=53.33&direction=DESC&orderByProperties=locationType,city&pageSize=5&pageNumber=0 
-> perform near query and return the first 5 result and order by them based on locationType and city in descending format
```

```
/search?longitude=3.911&latitude=53.33&pageSize=5&pageNumber=0&search=street=re="De";city=re="b"
-> return near stores which has a De in their street name *and* b in their city name
```
```
/search?longitude=3.911&latitude=53.33&pageSize=5&pageNumber=0&search=street=re="De",city=re="b"
-> return near stores which has a De in their street name *or* b in their city name
```
```
/search?longitude=3.911&latitude=53.33&pageSize=5&pageNumber=0&search=locationType=="SupermarktPuP"
-> return near stores which has locationType equals to SupermarktPuP
```
```
/search?longitude=3.911&latitude=53.33&pageSize=5&pageNumber=0&search=locationType=="SupermarktPuP";addressName=re="Jumbo"
-> return near stores which has locationType equals to SupermarktPuP and in addressName contains Jumbo word
```
```
/search?longitude=3.911&latitude=53.33&direction=ASC&orderByProperties=street&pageSize=5&pageNumber=0&search=locationType=="SupermarktPuP";addressName=re="Jumbo"
-> return near stores which has locationType equals to SupermarktPuP and in addressName contains Jumbo word and order by them based on street field
```

### RSQL

search query param 
1. 1. contains fields with operator and value. for instance: there is an entity with two fields ‘name’ and ‘title’ and we want to resolve an entity with a name equal to ‘John' and a title not equal to ‘Professor’ so the search param will be=> name==john;title!=Professor
2. operator
1. and fields: ;
2. or fields: ,
3. equal: == , examples: city==Eindhoven
4. not equal: !=
5. greater than: "=gt="
6. greater than equal: "=ge="
7. less than: "=lt="
8. less than or equal: "=le="
9. in: "=in=" , examples: city=in=(Harderwijk,Middelbeers,Woudenberg)
10. out: "=out=" , examples: city=out=(Harderwijk,Middelbeers,Woudenberg)
11. like: "=re=", example: street=re=Jumbo, it means streets that has a Jumbo in their names
### Swagger
```
http://{{host-name}}:{{mapped-port}}/customer-panel/swagger-ui/#/store-controller
```


### How to run it

Stack.yml

```yml
version: '3.1'

services:
  backend:
    container_name: jumbo
    image: erfancreed/jumbo:1.0.0
    restart: always
    ports:
      - 8585:8585
    environment:
      server.address: 0.0.0.0
      spring.data.mongodb.username: root
      spring.data.mongodb.password: test
      spring.data.mongodb.host: db
      spring.data.mongodb.port: 27017
      spring.data.mongodb.database: db
      logs.directory: /var/log/jumbo
    depends_on:
      - "db"
  db:
    container_name: mongo
    image: mongo:4.4.14
    restart: always
    environment:
      MONGO_INITDB_ROOT_USERNAME: root
      MONGO_INITDB_ROOT_PASSWORD: test
    ports:
      - 27017:27017
```
Run it with command --> docker-compose -f stack.yml up
