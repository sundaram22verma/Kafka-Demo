# Kafka Demo Application – Spring Boot

This project is a simple Spring Boot application demonstrating Kafka Producer and Kafka Consumer functionality using REST endpoints.
It allows you to:
- Send messages to a Kafka topic
- Automatically consume messages using a Kafka listener
- Retrieve consumed messages via REST API
- Perform simple health checks

---

## 🚀 Project Structure

```
src/main/java/com/demo/kafkademo
│
├── KafkaDemoApplication.java          # Main Spring Boot application
│
├── controller
│   └── KafkaController.java           # REST endpoints (/send, /messages, etc.)
│
├── service
│   └── KafkaProducerService.java      # Producer logic using KafkaTemplate
│
└── listener
    └── KafkaConsumerListener.java     # Kafka consumer using @KafkaListener
```

---

## 🧩 How the Application Works

### 1. Application Startup

- `KafkaDemoApplication` boots the Spring application.
- Spring automatically configures and creates all Kafka-related beans (`KafkaTemplate`, `KafkaListenerContainerFactory`, etc.).
- A log confirms that the application has started: `Kafka Demo Application Started on port 8080`.

### 2. Sending Messages (Producer Flow)

- **Endpoint**: `POST http://localhost:8080/kafka/send?message=HelloWorld`
- **Flow**:
    1. `KafkaController.sendMessage()` receives the HTTP request.
    2. It calls `KafkaProducerService.sendMessage()`.
    3. The `KafkaTemplate` asynchronously sends the message to the `demo-topic`.
    4. A `CompletableFuture` callback logs whether the send operation was successful or failed.

### 3. Consuming Messages (Consumer Flow)

- The `KafkaConsumerListener` class contains a method annotated with `@KafkaListener`.
  ```java
  @KafkaListener(topics = "demo-topic", groupId = "my-group")
  public void listen(String message) { ... }
  ```
- The `listen` method is automatically invoked by Spring Kafka whenever a new message arrives on `demo-topic`.
- Consumed messages are stored in a thread-safe list (`CopyOnWriteArrayList`) for later retrieval.

### 4. Retrieving Consumed Messages

- **Endpoint**: `GET http://localhost:8080/kafka/messages`
- **Returns**: A JSON array of all messages consumed by the listener since the application started.
  ```json
  [
    "HelloWorld",
    "Another message",
    "Kafka demo!"
  ]
  ```

### 5. Health & Utility Endpoints

| Endpoint                  | Description                               |
| ------------------------- | ----------------------------------------- |
| `GET /kafka/ping`         | Checks if the application is running.     |
| `GET /kafka/health`       | A simple health check, returns `"UP"`.    |
| `GET /kafka/messages`     | Lists all consumed messages.              |

---

## ⚙️ Kafka Configuration (application.properties)

To run this application, you must configure the Kafka bootstrap server in `src/main/resources/application.properties`.

```properties
spring.kafka.bootstrap-servers=localhost:9092

# Consumer Configuration
spring.kafka.consumer.group-id=my-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer

# Producer Configuration
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-deserializer=org.apache.kafka.common.serialization.StringSerializer
```

---

## ▶️ How to Run the Project

### 1. Start Kafka & Zookeeper

You need a running Kafka instance. Below are instructions for running Kafka locally.

**Option 1: Start Kafka without ZooKeeper (KRaft mode - Recommended)**

First, generate a cluster UUID:
```bash
# For Windows
.\bin\windows\kafka-storage.bat random-uuid

# For Linux/macOS
./bin/kafka-storage.sh random-uuid
```

Next, format your storage directory (run this only once):
```bash
# For Windows (replace <uuid> with the one you generated)
.\bin\windows\kafka-storage.bat format --standalone -t <uuid> -c config\server.properties

# For Linux/macOS (replace <uuid> with the one you generated)
./bin/kafka-storage.sh format --standalone -t <uuid> -c config/server.properties
```

Finally, start the Kafka server:
```bash
# For Windows
.\bin\windows\kafka-server-start.bat config\server.properties

# For Linux/macOS
./bin/kafka-server-start.sh config/server.properties
```

**Option 2: Start Kafka with ZooKeeper (Legacy)**
```bash
# For Windows
.\bin\windows\zookeeper-server-start.bat config\zookeeper.properties
.\bin\windows\kafka-server-start.bat config\server.properties

# For Linux/macOS
./bin/zookeeper-server-start.sh config/zookeeper.properties
./bin/kafka-server-start.sh config/server.properties
```

### 2. Start the Spring Boot application

Run the application using your IDE or via the Maven wrapper:
```bash
mvn spring-boot:run
```

### 3. Test the Endpoints

- **Send a message:**
  `POST http://localhost:8080/kafka/send?message=HelloKafka`
- **Get all consumed messages:**
  `GET http://localhost:8080/kafka/messages`

---

## 📌 Message Flow Overview

```mermaid
graph TD
    A[Client (Send Message)] --> B(KafkaController);
    B --> C(KafkaProducerService);
    C --> D[Kafka Broker (demo-topic)];
    D --> E(KafkaConsumerListener);
    E --> F[Stored in messages list];
    G[Client (Get Messages)] --> H(KafkaController);
    H --> F;
    F --> I[Returns list of consumed messages];
```

---

## 🛠️ Technologies Used

- Java 17+
- Spring Boot
- Spring Kafka
- Apache Kafka
- REST APIs (Spring MVC)