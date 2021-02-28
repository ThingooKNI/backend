# Thingoo backend (REST API + auth server)

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1fb45148a6bb40adb925f48609c37194)](https://app.codacy.com/gh/ThingooKNI/backend?utm_source=github.com&utm_medium=referral&utm_content=ThingooKNI/backend&utm_campaign=Badge_Grade_Settings)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1fb45148a6bb40adb925f48609c37194)](https://app.codacy.com/gh/ThingooKNI/backend?utm_source=github.com&utm_medium=referral&utm_content=ThingooKNI/backend&utm_campaign=Badge_Grade_Settings)

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
<https://app.swaggerhub.com/apis/Thingoo/thingoo-rest_api>
