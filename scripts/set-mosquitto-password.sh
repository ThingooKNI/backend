#!/bin/bash

if [[ $# -lt 2 ]]; then
    echo 'Not enough arguments given'
    exit 0
fi

touch mosquitto/mosquitto.passwd
mosquitto_passwd -b mosquitto/mosquitto.passwd "$1" "$2"