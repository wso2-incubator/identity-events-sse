## SSE Event Handler

### Introduction / Use case.

This SSE event handler will subscribe to POST_UPDATE_CREDENTIAL_BY_ADMIN events and it will communicate the event to the
SSE component, which is written as a separate service outside of IS. Refer \[1] for more details.

### Applicable product versions.

Tested with IS-5.7.0

### How to use.

1) Stop the server if it is already running.
2) Build the project using following command,
   ```mvn clean install```
3) Copy the jar file __org.wso2.custom.event.handler-1.0-SNAPSHOT.jar__ from the target directory to __<IS_HOME>
   /repository/components/dropins__ folder.
4) This event handler should be configured and subscribe to the POST_UPDATE_CREDENTIAL_BY_ADMIN event. Open the
   identity-event.properties, in __<IS_HOME>/repository/conf/identity/identity-event.properties__ file and add the
   following entries.
   ```module.name.13=custom.event.handler
      custom.event.handler.subscription.1=POST_UPDATE_CREDENTIAL_BY_ADMIN
    ```
   In this example 'module.name.13' is the next available module number, When you configure find the last '
   module.name.#' and configure 'custom.event.handler' with the last module number +1
5) Start the server.

### Testing the project.

1) Login to the IS management console. Eg : login with admin user credentials
2) In the carbon logs you should be able to see

```
INFO {SseEventHandler} -  SSE event handler received event POST_UPDATE_CREDENTIAL_BY_ADMIN.
INFO {SseEventHandler} -  Authenticated user : admin

```

\[1] - https://medium.com/@isurakarunaratne/wso2-identity-server-eventing-framework-32505bcc1600




