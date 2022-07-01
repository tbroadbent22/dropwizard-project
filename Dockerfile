# syntax=docker/dockerfile:1
FROM dockerhub-public.svsartifactory.swinfra.net/cafapi/opensuse-jre11:3
COPY . /src
WORKDIR /src
CMD java -jar target/dropwizard-project-1.0-SNAPSHOT.jar server properties.yml
EXPOSE 8080
