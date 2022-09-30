Camunda Platform 8 with Spring Boot Demo
---

This project aims at showcasing how the following technologies and architectural principles can be used together in
an example application.

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

- TripFlow applicaiton: http://localhost:8080/trip
- Camunda Self-Managed instance (with Hazelcast exporter): localhost, ports: 26500 (and others)
- Zeebe Simple Monitor: http://localhost:8082/
- Tasklist application: http://localhost:8081/
- Elastic search: localhost, port 9200
- Postgres database: localhost, port 5432, database: `tripflow`

## References

Here is the list of main references consulted while working on this project. Please, see JavaDoc in the relevant source
code for more detailed references.

1. [Camunda Platform, Getting Started, Spring Boot client](https://github.com/camunda/camunda-platform-get-started/blob/main/spring/src/main/java/io/camunda/getstarted/ProcessApplication.java)
2. [Camunda Platform 8, GraphQL API Tasklist, Java client](https://github.com/camunda-community-hub/camunda-tasklist-client-java)
3. [Revisiting Cargo tracking with Clean Architecture](https://github.com/gushakov/cargo-clean)

## Credits for the sample data and images

To have somewhat realistic data samples for flights, hotels, etc., the following resources (publicly available).

1. [Google Travel](https://www.google.com/travel)
2. [Unsplash images](https://unsplash.com/), please, also consult [Unsplash licence](https://unsplash.com/license)

## Notes

1. For user tasks, we should use "Candidate groups" to assign such and such task to a group of users (depending on their
role in the application.). Then, when completing the task, we should first `claim` the task first (using `CamundaTaskListClient`)
and, then, `complete` the task.
2. When entering "Conditional expression" for a flow branch of an exclusive gateway in Camunda Modeler, be careful to put 
a space between "=" and the beginning of the expression, like here: 
```xml
<bpmn:sequenceFlow id="Flow_09ivbqx" name="sufficient credit" sourceRef="Gateway_0svoha3" targetRef="Activity_0lzh8es">
  <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">= sufficientCredit</bpmn:conditionExpression>
</bpmn:sequenceFlow>
```
Otherwise, you get XML parsing exception when deploying the BPMN to Camunda.