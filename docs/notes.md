## Notes

- For user tasks, we should use "Candidate groups" to assign such and such task to a group of users (depending on their
  role in the application.).

- When using TaskList client and completing a task, we should first `claim` the task first (
  using `CamundaTaskListClient`)
  and, then, `complete` the task.

- When entering "Conditional expression" for a flow branch of an exclusive gateway in Camunda Modeler, be careful to put
  a space between "=" and the beginning of the expression, like in the example below. Otherwise, you get XML parsing
  exception when deploying the BPMN to Camunda.

```xml

<bpmn:sequenceFlow id="Flow_09ivbqx" name="sufficient credit" sourceRef="Gateway_0svoha3" targetRef="Activity_0lzh8es">
    <bpmn:conditionExpression xsi:type="bpmn:tFormalExpression">= sufficientCredit</bpmn:conditionExpression>
</bpmn:sequenceFlow>
```

- When executing a use case for a service task, we cannot access a logged-in user since we are not in the
  web authenticated context anymore. User related data should be accessed using `tripStartedBy` process variable.

- Candidate groups can be accessed from `ActivatedJob` via custom header: "io.camunda.zeebe:candidateGroups". This
  header will contain one string representing the array of groups in the form `"[\"FOOBAR\",\"WAMBAZ\"]"` (serialized JSON
  array).