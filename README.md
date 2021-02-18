# Thingoo backend (REST API + auth server)

## Requirements:
- Docker
- Docker compose
- JDK 11+
- *ktlint (for formatting, optional)*

## Running app for development
```shell
./scripts/start-dev.sh
```

## Applying code changes:
```shell
./scripts/reload-dev.sh
```

## Stopping the app
```shell
./scripts/stop-dev.sh
```

## Format code with ktlint
```shell
./scripts/format.sh
```

## Project documentation
[https://www.notion.so/Thingoo-24b12d89c3d644c8ba88c1c8ac29c38d]()

## Deployment
Backend is deployed on Kubernetes cluster and is accessible through following URLs:
- REST API: [https://dev.thingoo.xyz/api/]()
- Auth server (keycloak): [https://dev.thingoo.xyz/auth/]()

Default credentials for Keycloak's admin console:

username: admin

password: password

## Postman Collection
For easier client's implementation, we provide Postman collection with all endpoints provided by the API:

[https://app.getpostman.com/join-team?invite_code=e7a9f0d3527e5f429ee64eaf84d723dc]()

## OpenAPI Specification (Swagger)
[https://app.swaggerhub.com/apis/Thingoo/thingoo-rest_api]()