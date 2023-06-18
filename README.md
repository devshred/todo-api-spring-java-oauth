# Spring Boot 3.0 with OAuth 2.0 Resource Server
This project uses Spring Boot and OAuth 2.0 Resource Server to provide a simple todo-app API and is used for workshops.
It implements an [OpenAPI spec](src/main/spec/todo-spec.yaml) and uses Keycloak for authentication.

## Start infrastructure (incl. Keycloak)
```shell
docker compose -f infrastructure/docker-compose.yaml up 
```

# Set up Keycloak
* Create realm (TodoApp)
* Create client (todo-app-client)
  * Valid redirect URIs: http://localhost:8080/*
  * Web origins: +
  * Create role for client todo-app-client (user, admin)
  * Create realm roles (app_user, app_admin)
  * Select role, Action: Add associated role, Filter by clients, <client-name> / <role-name>
  * Create new user, Credentials, Set password (Temporary off), Role mapping, Assign role