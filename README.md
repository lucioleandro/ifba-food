# IFBA FOOD

## 🚀 Starting

### 📋 Prerequisites

```
Docker
```
O projeto contém um Dockerfile e um arquivo docker-compose, então você deve ter o Docker instalado em sua máquina.

Considerando que você tenha o Docker e o docker-compose instalados em sua máquina, o que você precisa fazer é:

Ir para a raiz do projeto e executar:

```
docker-compose up -d
```
O arquivo docker-compose.yml acima define uma série de serviços, redes e volumes para criar um ambiente de desenvolvimento para um sistema de microserviços. Vamos analisar cada serviço, rede e volume:

discovery-server: Este serviço é responsável por construir e executar o servidor de descoberta (geralmente usado para a comunicação entre microserviços). Ele expõe a porta 8081 e faz parte da rede "dev".

gateway: Este serviço atua como um gateway para os microserviços. Ele se conecta ao servidor de descoberta e depende de outros serviços como pagamentos, avaliação e pedidos. Ele expõe a porta 8082 e também faz parte da rede "dev".

pagamentos: Este é o serviço de pagamentos que se conecta ao banco de dados de pagamentos (database-pagamentos) e ao servidor de descoberta. Ele também se conecta ao serviço RabbitMQ para mensagens e faz parte da rede "dev". Um volume chamado mysql-data é montado para persistir os dados do MySQL.

avaliacao: Este serviço é responsável pela avaliação e se conecta ao servidor de descoberta e ao RabbitMQ. Ele faz parte da rede "dev".

pedidos: Este serviço gerencia pedidos e se conecta ao banco de dados de pedidos (database-pedidos), ao servidor de descoberta e ao RabbitMQ. Ele faz parte da rede "dev" e monta um volume chamado postgres-data para persistir os dados do PostgreSQL.

database-pagamentos: Este serviço é um banco de dados MySQL para armazenar informações de pagamento. Ele expõe a porta 3306 e faz parte da rede "dev".

database-pedidos: Este serviço é um banco de dados PostgreSQL para armazenar informações de pedidos. Ele expõe a porta 5432 e faz parte da rede "dev".

rabbitmq: Este serviço é um servidor RabbitMQ para gerenciamento de mensagens entre os microserviços. Ele expõe as portas 5672 e 15672, e faz parte da rede "dev". Um volume chamado dados é montado para persistir os dados do RabbitMQ.

Além disso, o arquivo define duas redes, "dev" e uma ponte de driver, e dois volumes chamados "mysql-data" e "postgres-data" para persistência de dados. Os serviços são configurados para reiniciar em caso de falha (restart: on-failure).

#### Postman

**Teste a API no postman clicando no botão abaixo**


[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/8603868-4f0eb1d1-554a-47b0-b6d4-39dc615d9187?action=collection%2Ffork&collection-url=entityId%3D8603868-4f0eb1d1-554a-47b0-b6d4-39dc615d9187%26entityType%3Dcollection%26workspaceId%3De0208d98-29dd-4f1e-b969-d6e0650e7991)



## ⚙️🛠️ Algumas Informações Sobre o Projeto

Construido usando:

- Java 17
- Springboot
- Docker
- Mysql 
- Postgres
- RabbitMQ

## Comandos Curl Para você testar via terminal.

### Consultar todos os pedidos

````
curl --location 'http://localhost:8082/pedidos-ms/pedidos'
````

### Realizar pedido

```
curl --location 'http://localhost:8082/pedidos-ms/pedidos' \
--header 'Content-Type: application/json' \
--data '{
    "itens": [
    {
        "quantidade": 10,
        "descricao": "Coca-cola"
    },
    {
        "quantidade": 5,
        "descricao": "Mc Chicken"
    }
    ]
}'
```

### Consultar pagamentos

````
curl --location 'http://localhost:8082/pagamentos-ms/pagamentos'
````

### Criar pagamento

````
curl --location 'http://localhost:8082/pagamentos-ms/pagamentos' \
--header 'Content-Type: application/json' \
--data '{
    "valor": 90090,
    "nome": "Renato",
    "numero": "12345678",
    "expiracao": "10/29",
    "codigo": "123",
    "pedidoId": 1,
    "formaDePagamentoId": 7
}'
````

### Aprovar Pagamento

````
curl --location --request PATCH 'http://localhost:8082/pagamentos-ms/pagamentos/1/confirmar' \
--header 'Content-Type: application/json' \
--data '{
    "valor": 900,
    "nome": "Renato",
    "numero": "12345678",
    "expiracao": "10/29",
    "codigo": "123",
    "pedidoId": 1,
    "formaDePagamentoId": 1,
    "status": "CRIADO"
}'
````

OBS: Não esquecer de checar os parametros para fazer as requisições de acordo...

