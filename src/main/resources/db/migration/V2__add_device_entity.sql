CREATE TABLE devices(
    id SERIAL PRIMARY KEY,
    device_id TEXT NOT NULL UNIQUE,
    display_name TEXT,
    mac_address TEXT NOT NULL UNIQUE
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

CREATE INDEX idx_entities_key ON entities(key);