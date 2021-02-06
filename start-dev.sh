#!/bin/bash

set -e

docker-compose down
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d --build