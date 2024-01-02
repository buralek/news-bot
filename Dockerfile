FROM openjdk:21

ADD ./news-bot-webapp/target/news-bot-webapp-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:2110", "-jar", "/app/news-bot-webapp-0.0.1-SNAPSHOT.jar"]

EXPOSE 2010
