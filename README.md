# wrapper
This application was generated using start.spring.io, This is a "monolithic" application intended to be part of a SOA architecture.

## Software Required
 Java 8
 IDE
 Maven
 MongoDB

## Java version
 The project requires Java 8 installed.  
 Remember to set the `JAVA_HOME` to use the correct JDK
 
## Database setup
* Build a schema with name `wrapper`  
  OR  
  Change database properties in `application.properties` as required.  
  Basically we should have an empty schema with configs specified in application.properties.   
  
**NOTE:** Don't create any tables(as JPA will take care of that)

## Dependencies
 a. Core - 
 		git url- https://gitlab.kelltontech.net/vestige/core.git
 b. soap-api
 		git url - https://gitlab.kelltontech.net/vestige/soapapi.git
 		
 **NOTE:** take pull in the same promotion service folder.

## git
Create a folder where your want to setup your local project.
1. Open Git Bash
2. $ cd CreatedFolder
3. $ git clone https://gitlab.kelltontech.net/vestige/wrapper.git
4. $ cd wrapper/  
(You are now in master)
5. $ git checkout develop

## Development

To start your application in the dev profile, simply run:

    ./mvnw



## Using services
*  login service request:
   Create a post request for `http://localhost:9090/login` with:  
   Content-Type as application/json  
   Body as {"userName":"11000076","password":"1234"}  
   With valid credentials, It should response with a token.
   
   **NOTE:** credentials may change in future.

