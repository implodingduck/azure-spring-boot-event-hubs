# azure-spring-boot-event-hubs
## Prerequisites
```
az ad sp create-for-rbac --name spring-boot-event-hubs
```

## Running and Testing

Start:
```
 ./mvnw spring-boot:run
 ```

Test:
 ```
curl -v -H "Content-Type: application/json" -X POST -d '{"message": "hello world"}' http://localhost:8080/api/producer/
```

## Resources:
* https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-cloud-stream-binder-java-app-kafka-azure-event-hub
* https://github.com/Azure-Samples/keda-eventhub-kafka-scaler-terraform

