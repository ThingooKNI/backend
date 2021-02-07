#!/bin/bash

kubectl create configmap keycloak-config --from-file config/import.json --dry-run=client --output=yaml