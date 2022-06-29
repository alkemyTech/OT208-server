# 🚀 OT208 Rocket Team - ONG-server 
<div style="display: inline_block">
  <a href="https://github.com/topics/java" target="_blank"><img align="center" alt="Made With JAVA"  src="https://img.shields.io/badge/Made%20With-Java-blue"></a>
  <a href="#coverage"><img align="center" alt="COVERAGE"  src="https://s3.amazonaws.com/assets.coveralls.io/badges/coveralls_100.svg"></a>
  <a href="https://documenter.getpostman.com/view/21639215/UzBsHj42" target="_blank"><img align="center" alt="POSTMAN DOC"  src="https://img.shields.io/badge/Postman-ApiDoc-orange"></a>
  <a href="https://github.com/alkemyTech/OT208-server/graphs/contributors" target="_blank"><img align="center" alt="COMMITS/M"  src="https://img.shields.io/github/commit-activity/m/alkemyTech/OT208-server"></a>
</div>
<div style="display: inline_block"><br><br>
  <a href="https://github.com/alkemyTech" target="_blank"><img align="center" alt="ALKEMY" height="84" width="84" src="https://user-images.githubusercontent.com/85143329/175833035-20a3a828-2bb5-4919-88b9-d90efdd36074.png"></a>
  <a href="https://github.com/topics/java" target="_blank"><img align="center" alt="JAVA" height="84" width="84" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg"></a>
  <a href="https://github.com/topics/spring-boot" target="_blank"><img align="center" alt="SPRING" height="84" width="84" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg"></a>
  <a href="https://github.com/topics/mysql" target="_blank"><img align="center" alt="MYSQL" height="84" width="84" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg"></a>
</div>


----

**ONG-server is a REST API in JAVA 11 with Spring Boot 2.5.1**

Rocket Team:
* :colombia: Nestor Raul Agredo  [LinkedIn](https://www.linkedin.com/in/nestor-raul-agredo-llanten/ ) - [GitHub](https://github.com/nestoragredollanten)
* :peru: Eduardo Sanchez [LinkedIn](https://www.linkedin.com/in/eduardo-sanchez-038086232/ ) - [GitHub](https://github.com/EdwardDavys)
* :argentina: Oriana Pellegrini [LinkedIn](https://www.linkedin.com/in/oriana-pellegrini/ ) - [GitHub](https://github.com/Oriana10)
* :argentina: Martin Iriarte [LinkedIn](https://www.linkedin.com/in/martin-iriarte/ ) - [GitHub](https://github.com/MartinIriarte89)
* :argentina: Adrian Camus [LinkedIn](https://www.linkedin.com/in/acamus79/ ) - [GitHub](https://github.com/acamus79)

Mentor: Juan Esteban Lopez [LinkedIn](https://www.linkedin.com/in/juan-esteban-lopez-8bb677b/ ) - [GitHub](https://github.com/juan1977lopez)

As part of Alkemy's acceleration for Java BackEnd. Implements functionalities for an NGO and its online management through a portal that allows access with security and differentiated by roles of Users and Administrators, facilitating the administration of partners, news, comments or contacts received from the Organization, and other features such as storing files in an AWS S3 bucket depending on the functionality, as well as the automatic sending of emails.


-----

**Entity Relationship Diagram**

<br>

![ERot208](https://user-images.githubusercontent.com/85143329/175832651-337fb842-e15b-4476-b2b4-1cfa9957d663.png)

-----

In order to meet all specifications, the following dependencies were implemented:


|       Dependency        |      Link   |
|:-----------------------:|:-----------:|
|Sendgrid 4.7.2           |<a href="https://github.com/sendgrid/sendgrid-java" target="_blank">Java Sendgrid</a>|
|JsonWebToken 0.9.1       |<a href="https://github.com/auth0/java-jwt" target="_blank">Java JWT</a>|
|Amazon AWS SDK 1.12.220  |<a href="https://github.com/aws/aws-sdk-java" target="_blank">AWS SDK</a>|
|JavaLombok 1.18.20       |<a href="https://github.com/projectlombok/lombok" target="_blank">Proyect Lombok</a>|
|Sprindoc OpenApi UI 1.6.8|<a href="https://github.com/springdoc/springdoc-openapi" target="_blank">Springdoc OpenApi</a>|
|MySQL 8.0.25             |<a href="https://dev.mysql.com/doc/" target="_blank">MySQL Doc</a>|
|ModelMapper 3.1.0        |<a href="https://github.com/modelmapper/modelmapper" target="_blank">ModelMapper</a>|


-----

Of the 110 Classes developed, one of them is a Seeder in which users with different roles are created, 10 users with Administrative permissions and 10 with basic User permissions. 

<br>

**User List**

|   Email / Username   | First Name |	Last Name |	Password |	Photo |	Role  |
|:--------------------:|:----------:|:---------:|:--------:|:------:|:-----:|
|admin@mail.com        | Admin      | Root      | 12345678 |user.png|ADMIN  |
|user@mail.com         | User       | User      | 12345678 |user.png|USER   |
|heidicannon@mail.com  | Heidi      | Cannon    | 12345678 |user.png|ADMIN  |
|christinesimpson@mail.com|Christine|Simpson    | 12345678 |user.png|USER   |
|edwardwood@mail.com   | Edward     | Wood      | 12345678 |user.png|ADMIN  |
|allisonproctor@mail.com| Allison   | Proctor   | 12345678 |user.png|USER   |
|dustinsmith@mail.com  | Dustin     | Smith     | 12345678 |user.png|ADMIN  |
|juliewilson@mail.com  | Julie      | Wilson    | 12345678 |user.png|USER   |
|robertahahn@mail.com  | Roberta    | Hahn      | 12345678 |user.png|ADMIN  |
|jamesbrooks@mail.com  | James      | Brooks    | 12345678 |user.png|USER   |
|rosebaldwin@mail.com  | Rose       | Baldwin   | 12345678 |user.png|ADMIN  |
| sarahbarr@mail.com   | Sarah      | Barr      | 12345678 |user.png|USER   |
|brookeadkins@mail.com | Brooke     | Adkins    | 12345678 |user.png|ADMIN  |
|robertmorgan@mail.com | Robert     | Morgan    | 12345678 |user.png|USER   |
|jessicahodge@mail.com | Jessica    | Hodge     | 12345678 |user.png|ADMIN  |
|geraldhunter@mail.com | Gerald     | Hunter    | 12345678 |user.png|USER   |
|courtneygill@mail.com | Courtney   | Gill      | 12345678 |user.png|ADMIN  |
|rachelturner@mail.com | Rachel     | Turner    | 12345678 |user.png|USER   |
|stephenmorris@mail.com| Stephen    | Morris    | 12345678 |user.png|ADMIN  |
|nancycooper@mail.com  | Nancy      | Cooper    | 12345678 |user.png|USER   |

<br>

-----

**Documentation**

Through Postman and Swagger

|       Document        |      Link   |
|:-----------------------:|:-----------:|
| POSTMAN |<a href="https://documenter.getpostman.com/view/21639215/UzBsHj42" target="_blank">LINK</a>|
| SWAGGER |<a href="http://localhost:8080/api/swagger-ui/index.html" target="_blank">LINK</a> (only if running)|

<br>

-----

**Testing Coverage**

<div id="coverage">
  <br>
  <div>The full HTML report can be accessed from the compressed file <a href="https://github.com/alkemyTech/OT208-server/raw/main/CoverageReport.zip" target="_blank">CoverageReport.zip</a> located in the project directory.</div>
  <br>
  
  <img align="center" alt="COVERAGE" src="https://user-images.githubusercontent.com/85143329/176202236-2d970474-17e3-40d7-82e4-03743f36e5fc.png">
</div>

-----

**Credentials required to compile**

To execute this project, before compiling, the application properties file must be completed in its corresponding sections:

* Specify a token secret key, and the token duration in milliseconds.
* Must have Sendgrid and AWS credentials which you must complete 

![imagen](https://user-images.githubusercontent.com/85143329/176469696-50668039-eba5-4d23-8a75-20d01b6e1f69.png)



