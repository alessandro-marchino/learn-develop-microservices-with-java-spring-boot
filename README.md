# Master Microservices with SpringBoot, Docker, Kubernetes

Companion repository for Udemy Course
[Master Microservices with SpringBoot, Docker, Kubernetes](https://www.udemy.com/course/master-microservices-with-spring-docker-kubernetes/).

## Commands

- To switch to java 21: `sdk use java 21.0.3-tem`
- To generate the images: `mvn compile jib:dockerBuild`
- To run just the dependencies for the microservices on Docker:
`dcupd accountsdb cardsdb loansdb redis keycloak kafka`

## Keycloak

URL for configuration endpoints: [http://localhost:7080/realms/master/.well-known/openid-configuration](http://localhost:7080/realms/master/.well-known/openid-configuration)

- Create a client with name `eazybank-callcenter-cc` for the `Service accounts roles`
authentication flow
- Create the `ACCOUNTS`, `CARDS` and `LOANS` Realm roles
- Assign the Service Account Roles to the client
- Copy the client secreto for use in Postman

## Kubernetes dashboard

Please refer to the [kubernetes/dashboard/README.md](kubernetes/dashboard/README.md) file
