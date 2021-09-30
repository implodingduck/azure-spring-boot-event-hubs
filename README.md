# azure-spring-boot-event-hubs
## Prerequisites
```
az ad sp create-for-rbac --name spring-boot-event-hubs
```


## Terraform
This sets up the event hub infrastructure and also the storage account to be used as a checkpointer
```
cd terraform
./env_setup.sh
terraform apply
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
* https://docs.microsoft.com/en-us/azure/event-hubs/
* https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-for-kafka-ecosystem-overview
* https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-cloud-stream-binder-java-app-kafka-azure-event-hub
* https://github.com/Azure-Samples/keda-eventhub-kafka-scaler-terraform
* https://github.com/eugenp/tutorials/tree/master/spring-kafka
* https://github.com/Azure/azure-event-hubs-for-kafka/tree/master/tutorials/oauth/java/managedidentity
* https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-cloud-stream-binder-java-app-azure-event-hub