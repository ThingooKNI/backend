CREATE TABLE readings(
    id SERIAL PRIMARY KEY,
    value TEXT NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE,
    entity_id INT REFERENCES entities (id) NOT NULL
);
