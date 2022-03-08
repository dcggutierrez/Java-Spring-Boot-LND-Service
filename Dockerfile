FROM adoptopenjdk/openjdk11 as builder

WORKDIR /app

COPY . .

RUN ./mvnw package 

FROM adoptopenjdk/openjdk11 as release

WORKDIR /app
ENV ARTIFACT_NAME=LND-Service-0.0.1.jar

COPY --from=builder ./app/target/${ARTIFACT_NAME} ./

EXPOSE 8080
CMD java -jar ${ARTIFACT_NAME}