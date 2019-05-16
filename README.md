# CoffeeGram
![Main Page](/screenshots/website.gif)  
**Project for - CMP416 - Internet and Network Computing**  
**Project Duration - March-May 2019**  
***
The website allows people to connect with each other and share their coffee-infested experiences. The target audience for this application is coffee lovers of all shapes and sizes. The website will allow people to post images of their coffees and add captions to them. Other users will also be able to view, like and comment on them.

The website uses Java Server Faces and Managed Beans for the frontend and backend. It also uses Java SQL Database for retrieval and storage of data. Finally, the website is hosted using Glassfish server.

## Requirements
* [Java 8 EE](https://www.java.com/en/download/)
* Any Java IDE ([Netbeans 8.2](https://netbeans.org/downloads/8.2/) was used in this project)

## Installation
1. Clone the repo by using the following command
``` bash
$ git clone https://github.com/hussu97/coffee-gram.git
$ cd coffee-gram
```
2. Open the project in your Java IDE
3. Make sure your GlassFish server is on and running
4. Open your Java DB and execute the commands stored in the ```db.sql``` file
5. Change the database configurations in the ```web/WEB-INF/faces-config.xml``` file, as shown below:
``` xml
<managed-bean>
        <managed-bean-class>Beans.Singleton</managed-bean-class>
        <managed-bean-name>sing</managed-bean-name>
        <managed-bean-scope>application</managed-bean-scope>
        <managed-property>
            <property-name>DB</property-name>
            <value>DATABASE_NAME</value>
        </managed-property>
        <managed-property>
            <property-name>user</property-name>
            <value>DATABASE_USERNAME</value>
        </managed-property>
        <managed-property>
            <property-name>password</property-name>
            <value>DATABASE_PASSWORD</value>
        </managed-property>
    </managed-bean>
```
6. Change the directory of where you will store your images to your local directory, in the ```web/WEB-INF/glassfish-web.xml``` file.
``` xml
<property name ="alternatedocroot_1" value ="from=*.jpg dir=path/to/images/folder"/> 
  <property name ="alternatedocroot_2" value ="from=*.png dir=path/to/images/folder"/> 
```
7. Build and Run the project
8. View the project at http://localhost:8080/

## Additional Screenshots

![Main](/screenshots/main.png)
***
![Profile](/screenshots/profile.png)
***
## License
[GNUv3](https://github.com/hussu97/coffee-gram/blob/master/LICENSE)
