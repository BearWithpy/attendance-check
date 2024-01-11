FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
ARG SPRING_PROFILE=local
COPY ${JAR_FILE} app.jar
ENV spring_profile=${SPRING_PROFILE}
ENTRYPOINT java -jar app.jar \
            --spring.profiles.active=${spring_profile}

EXPOSE 9989
#ENTRYPOINT ["java", "-jar", "app.jar"]
