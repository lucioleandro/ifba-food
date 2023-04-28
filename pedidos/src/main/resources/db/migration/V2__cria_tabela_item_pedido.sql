CREATE TABLE item_do_pedido
(
    id         SERIAL PRIMARY KEY,
    descricao  VARCHAR(255),
    quantidade INT    NOT NULL,
    pedido_id  BIGINT NOT NULL,
    CONSTRAINT fk_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos (id)
);
