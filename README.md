spring.application.name=Cms

# ===============================
# = DATABASE CONFIGURATION (MySQL)
# ===============================
spring.datasource.url=jdbc:mysql://localhost:3306/cms
spring.datasource.username=////////////////////////////data base username////////////////////////////
spring.datasource.password=////////////////////////////database password////////////////////////////
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate/JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.jdbc.time_zone=UTC

# Enable Hibernate SQL logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql=TRACE

# ===============================
# = JWT CONFIGURATION
# ===============================
jwt.secret=////////////////////////////key here////////////////////////////

jwt.cookie.name=accessToken
jwt.refreshCookie.name=refreshToken
jwt.cookie.secure=true # Set to true in production
jwt.cookie.httpOnly=true
jwt.cookie.path=/
jwt.cookie.domain=localhost # Change for production

# ===============================
# = SECURITY CONFIGURATION
# ===============================
logging.level.org.springframework.security=DEBUG

# ===============================
# = EMAIL CONFIGURATION (Gmail Example)
# ===============================
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=////////////////////////////your gmail here////////////////////////////
spring.mail.password=////////////////////////////your application password here////////////////////////////
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000

# ===============================
# = UPLOAD CONFIGURATION
# ===============================
project.uploads: uploads/
