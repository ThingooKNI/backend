# Thingoo backend (REST API + auth server)

## Requirements:
- Docker
- Docker compose
- JDK
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

## Deployment
Backend is deployed on Kubernetes cluster and is accessible through following URLs:
- REST API: [https://dev.thingoo.xyz/api/]()
- Auth server (keycloak): [https://dev.thingoo.xyz/auth/]()

Default credentials for Keycloak's admin console:

username: admin

password: password

## Postman Collection
For easier client's implementation, we provide Postman collection with all endpoints provided by the API:

(https://app.getpostman.com/join-team?invite_code=e7a9f0d3527e5f429ee64eaf84d723dc)[]