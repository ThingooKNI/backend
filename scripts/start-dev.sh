#!/bin/bash

set -e

./gradlew build -x test
docker-compose down
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d --build