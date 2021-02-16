CREATE TYPE entity_type AS ENUM ('SENSOR', 'ACTUATOR');

CREATE TYPE unit_type AS ENUM ('INTEGER', 'DECIMAL', 'STRING', 'BOOLEAN');

CREATE TABLE devices(
    id SERIAL PRIMARY KEY,
    device_id TEXT NOT NULL,
    display_name TEXT,
    mac_address TEXT NOT NULL
);

CREATE TABLE entities(
    id SERIAL PRIMARY KEY,
    display_name TEXT,
    key TEXT NOT NULL,
    type TEXT NOT NULL,
    unit_display_name TEXT NOT NULL,
    unit_type TEXT NOT NULL,
    device_id INT REFERENCES devices (id) NOT NULL
);