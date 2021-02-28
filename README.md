# Thingoo backend (REST API + auth server)

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e384bf6d86ec44e794c78e2d8923ecd7)](https://www.codacy.com/gh/ThingooKNI/backend/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ThingooKNI/backend&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/e384bf6d86ec44e794c78e2d8923ecd7)](https://www.codacy.com/gh/ThingooKNI/backend/dashboard?utm_source=github.com&utm_medium=referral&utm_content=ThingooKNI/backend&utm_campaign=Badge_Coverage)

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

## Watching application logs:
```shell
docker logs backend -f 
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
<https://www.notion.so/Thingoo-24b12d89c3d644c8ba88c1c8ac29c38d>

## Deployment
Backend is deployed on Kubernetes cluster and is accessible through following URLs:
- REST API: <https://dev.thingoo.xyz/api/>
- Auth server (keycloak): <https://dev.thingoo.xyz/auth/>

Default credentials for Keycloak's admin console:

username: admin

password: password

## Postman Collection
For easier client's implementation, we provide Postman collection with all endpoints provided by the API:

<https://app.getpostman.com/join-team?invite_code=e7a9f0d3527e5f429ee64eaf84d723dc>

## OpenAPI Specification (Swagger)
<https://app.swaggerhub.com/apis/BartlomiejRasztabiga/thingoo-rest_api/>
