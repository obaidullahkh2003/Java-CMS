
📚 CMS - Spring Boot Application Configuration

This project uses Spring Boot with MySQL, JWT authentication, and email support. Below is a breakdown of the essential configurations stored in the application.properties file.

------------------------------------------------------------
⚙️ Application Name
------------------------------------------------------------
spring.application.name=Cms

------------------------------------------------------------
🛢️ Database Configuration (MySQL)
Replace the placeholders with your actual credentials.
------------------------------------------------------------
spring.datasource.url=jdbc:mysql://localhost:3306/cms
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

------------------------------------------------------------
Hibernate / JPA
------------------------------------------------------------
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

------------------------------------------------------------
Hibernate SQL Logging
------------------------------------------------------------
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql=TRACE

------------------------------------------------------------
🔐 JWT Configuration
Replace YOUR_SECRET_KEY with a strong secret string.
------------------------------------------------------------
jwt.secret=YOUR_SECRET_KEY

jwt.cookie.name=accessToken
jwt.refreshCookie.name=refreshToken
jwt.cookie.secure=true
jwt.cookie.httpOnly=true
jwt.cookie.path=/
jwt.cookie.domain=localhost

------------------------------------------------------------
🔒 Spring Security Logging
------------------------------------------------------------
logging.level.org.springframework.security=DEBUG

------------------------------------------------------------
📧 Email Configuration (Using Gmail)
Replace with your Gmail address and app password.
------------------------------------------------------------
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_GMAIL
spring.mail.password=YOUR_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000

------------------------------------------------------------
📁 File Upload Configuration
------------------------------------------------------------
project.uploads=uploads/

------------------------------------------------------------
✅ Notes
------------------------------------------------------------
- DO NOT commit secrets or passwords to GitHub.
- Use environment variables or a .env file in production.
- Change jwt.cookie.domain to your domain when deployed.
- This configuration assumes MySQL 8.
