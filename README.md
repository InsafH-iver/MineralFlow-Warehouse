# Mineral Flow - Warehouse
Welcome to the **Mineral Flow - Warehouse** microservice, one of the three core components of the Mineral Flow solution. This service handles all warehouse-side operations including:

- **Inventory Management**:
    - Tracking total raw materials in the warehouse
    - Adjusting warehouse volume capacity (usedCapacity)
    - Updating stock levels accurately when materials are deposited or withdrawn
    - Automatically assigning oldest stock (FIFO) for loading to minimize storage charges

- **Purchase Order (PO) Processing**:
    - POs from buyers as shipping notifications
    - Tracking fulfilled vs. open POs 
    - Automatically calculating commission on fulfilled POs upon ship departure

- **Storage Billing**:
    - Modifying storage prices and cost prices
    - Calculating storage costs daily at 9:00 AM
    - Generating invoices for storage

## Set-up
### 1. Set Up the Database, Keycloak and RabbitMQ
1. **Database**:
    - Choose your preferred database (e.g., PostgreSQL, MySQL) and set it up.
    - Create a database for the application.
    - Note down the connection URL, username, and password for later configuration.

2. **Keycloak**:
    - Set up Keycloak.
    - Create a realm, client, and configure roles and users as per your requirements.

3. **RabbitMQ**:
    - Set up RabbitMQ
    - Set up RabbitMQ credentials (or use the default ones)
    - Update your application properties file with the connection details

### 2. Application properties
The properties that need to be updated in the [application properties file](./src/main/resources/application.properties) to your connection details are:
```properties
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

spring.rabbitmq.host=
spring.rabbitmq.port=
spring.rabbitmq.username=
spring.rabbitmq.password=
```

## Running the Application
1. Ensure all containers are running. (Keycloak, database, RabbitMQ)
2. Ensure all projects are pulled and set up correctly ([Land](https://github.com/InsafH-iver/MineralFlow-Land), Warehouse, Water)
3. Start the Spring Boot application.
4. Access the application at `http://localhost:8081` (backend API).

### Banner Configuration

This project uses a custom banner that appears in the console when the application starts. You can modify or disable the
banner by adjusting the `banner.txt` file or using the following settings in `application.properties`:

To disable the banner:

```properties
spring.main.banner-mode=off
```