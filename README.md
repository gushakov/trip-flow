Camunda Platform 8 with Spring Boot Demo
---

This project aims at showcasing how the following technologies and architectural principles can be used together in
an example application.

Here is a link to [an article on Medium](https://medium.com/@gushakov/af8733ec0024) which references this repository.

- Camunda Platform 8 (self-managed)
- BPMN workflow
- Spring Boot
- Clean Architecture
- DDD

## How to run

- Start the stack with `docker-compose up -d`
- Deploy BPMN diagram `bpmn/trip-flow.bpmn` to Camunda using Simple Monitor interface
- Run Spring Boot application (default profile) from the IDE

## What is running

- TripFlow applicaiton: http://localhost:8080/trip (username: `customer1` or `customer2`, password: `demo`)
- Camunda Self-Managed instance (with Hazelcast exporter): localhost, ports: 26500 (and others)
- Zeebe Simple Monitor: http://localhost:8082/
- Postgres database: localhost, port 5432, database: `tripflow`

## References

**Special thanks** to Camunda team who gave me some pointers about how to integrate Zeebe's Java client. [This GitHub
repo](https://github.com/camunda-community-hub/camunda-8-lowcode-ui-template) was especially helpful.

:star: As I am writing this, [CamundaCon 2022](https://www.camundacon.com/) is taking place! There is a great
presentation by Luc Weinbrecht, speaking exactly about how to use Camunda and Clean Architecture together.

Here is the list of main references consulted while working on this project. Please, see JavaDoc in the relevant source
code for more detailed references.

1. [Camunda Community Hub, Lowcode UI template example](https://github.com/camunda-community-hub/camunda-8-lowcode-ui-template)
2. [Luc Weinbrecht, GitHub, "Camunda DDD and Clean Architecture"](https://github.com/lwluc/camunda-ddd-and-clean-architecture)
3. [Camunda Platform, Getting Started, Spring Boot client](https://github.com/camunda/camunda-platform-get-started)
4. [Camunda Platform 8, GraphQL API Tasklist, Java client](https://github.com/camunda-community-hub/camunda-tasklist-client-java)
5. ["Ports & Adapters Architecture", Herberto Graça](https://herbertograca.com/2017/09/14/ports-adapters-architecture/)
6. ["Clean Architecture", Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
7. [Clean Domain-Driven Design](https://medium.com/@gushakov/clean-domain-driven-design-2236f5430a05)
8. [Revisiting cargo tracking application using Clean DDD](https://medium.com/@gushakov/revisiting-cargo-tracking-application-using-clean-ddd-4ed16c0e6ae1)
9. [Bernd Rücker, "Navigating Technical Transactions with Camunda 8 and Spring"](https://medium.com/berndruecker/navigating-technical-transactions-with-camunda-8-and-spring-d77d48f16ab9)

## Credits for the sample data and images

To have somewhat realistic data samples for flights, hotels, etc., the following resources (publicly available).

1. [Google Travel](https://www.google.com/travel)
2. [Unsplash images](https://unsplash.com/), please, also consult [Unsplash licence](https://unsplash.com/license)

