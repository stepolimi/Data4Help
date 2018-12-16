FROM tomee:8-jre-1.7.5-jaxrs

COPY target/rest-example-with-application-1.1.0-SNAPSHOT.war /usr/local/tomee/webapps/rest-example-with-application-1.1.0-SNAPSHOT.war

COPY config/tomcat-users.xml /usr/local/tomee/conf/tomcat-users.xml

CMD ["catalina.sh", "run"]