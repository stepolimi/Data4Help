FROM tomee:8-jre-8.0.0-M1-plus

COPY target/*.war /usr/local/tomee/webapps

COPY config/tomcat-users.xml /usr/local/tomee/conf/tomcat-users.xml

CMD ["catalina.sh", "run"]