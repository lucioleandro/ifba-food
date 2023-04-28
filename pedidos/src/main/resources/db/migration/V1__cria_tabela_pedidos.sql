CREATE TABLE pedidos
(
    id        SERIAL PRIMARY KEY,
    data_hora TIMESTAMP    NOT NULL,
    status    VARCHAR(255) NOT NULL
);
