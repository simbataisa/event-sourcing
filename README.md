# my-event-store-demo

## Architecture Diagram

![architecture_diagram](docs/resources/images/architecture_diagram.png)

## How to Run
1. Build your project
    ```shell
    ./gradlew clean build --info
    ```
2. Start Zookeeper
    ```shell
    zookeeper-server-start /Users/dennis.dao/workspace/app/kafka_2.13-2.8.0/config/zookeeper.properties
    ```

3. Start Kafka
    ```shell
    kafka-server-start /Users/dennis.dao/workspace/app/kafka_2.13-2.8.0/config/server.properties
    ```

4. *Run app with spring profile property `spring.profiles.active`:*

   * `api-gateway-service` = `default`
     * Access your *Eureka Naming Server* at http://localhost:8761/
     * User: `admin`
     * Password: `password`
   
   * `naming-server` = `default`

   * Option 1:
     * `event-storage-service` connects to *kafka* cluster topic to process the event.
       * `default` or `h2`
       * `rocksdb`
     * `command-service` = `camel-kafka`
       * with remote or local kafka broker:
         * local loopback host: `spring.kafka.bootstrap-servers=localhost:9092`
         * remote ip: `spring.kafka.bootstrap-servers=192.168.1.19:9092`
     * `query-service` = `default` or `event-store`
   * Option 2:
     * `event-storage-service` = `event-store`
     * `command-service` = `event-store`
       * This will call `event-store-service` endpoints to process event instead of using `kafka` broker.
     * `query-service` = `default` or `event-store`


**When using remote broker, the remote kafka `advertised.listeners` or `listeners` in `server.properties` should be set to the machine ip address. If not it will use "localhost/127.0.0.1"**

## How to test

### Postman Collection

Please import the collection from link:docs/resources/CQRS_Event_Sourcing.postman_collection.json[here].

image::docs/resources/postman.png[]

### Using `curl` command

1. Create `Board`
    ```shell
    curl --location --request POST 'http://localhost:9080/my-event-store-command/boards/' \
    --header 'Cookie: JSESSIONID=C3FD1BBEE36328C536EF9ED0B7CB5BC8'
    ```

2. Get `Board`
    ```shell
    curl --location --request GET 'http://localhost:9080/my-event-store-command/boards/9b0415a2-d13a-4af3-8fee-9c902d47cc13' \
    --header 'Cookie: JSESSIONID=C3FD1BBEE36328C536EF9ED0B7CB5BC8'
    ```

3. Rename `Board`
    ```shell
    curl --location --request PATCH 'http://localhost:9080/my-event-store-command/boards/9b0415a2-d13a-4af3-8fee-9c902d47cc13?name=dennis 3' \
    --header 'Cookie: JSESSIONID=C3FD1BBEE36328C536EF9ED0B7CB5BC8'
    ```

4. Add `Story to Board`
    ```shell
    curl --location --request POST 'http://localhost:9080/my-event-store-command/boards/9b0415a2-d13a-4af3-8fee-9c902d47cc13/stories' \
    --header 'Cookie: JSESSIONID=C3FD1BBEE36328C536EF9ED0B7CB5BC8' \
    --form 'name="store 7"'
    ```

5. Update `Story`
    ```shell
    curl --location --request PUT 'http://localhost:9080/my-event-store-command/boards/9b0415a2-d13a-4af3-8fee-9c902d47cc13/stories/fb7f25d5-3a68-4ab9-9aa9-3546e8847091?name=dennis story 1' \
    --header 'Cookie: JSESSIONID=C3FD1BBEE36328C536EF9ED0B7CB5BC8' \
    --form 'name="store 3"'
    ```

6. Delete `Story`
    ```shell
    curl --location --request DELETE 'http://localhost:9080/my-event-store-command/boards/9b0415a2-d13a-4af3-8fee-9c902d47cc13/stories/fb7f25d5-3a68-4ab9-9aa9-3546e8847091' \
    --header 'Cookie: JSESSIONID=C3FD1BBEE36328C536EF9ED0B7CB5BC8'
    ```

## RSocket CLI

You can use https://github.com/rsocket/rsocket-cli[`rsocket-cli`] to test the `rsocket` endpoints

1. Install `rsocket-cli`  via `Homebrew` (this does not work for Apple M1) or checkout the code and build it locally.
    ```shell
    brew install yschimke/tap/rsocket-cli
    ```
   
    or
    ```shell
    ./gradlew --console plain installDist
    ```

2. Connect to `spring` rsocket server and `route` with following command (`stream`, `request`)
    ```shell
    rsocket-cli --stream --debug --route=/my-event-store-query/rs/domain-event-stream ws://localhost:9981/rs
    
    rsocket-cli --request --debug --route=/my-event-store-query/rs/boards -i 04474929-5929-4e73-8b87-39feb7a15e6f ws://localhost:9981/rs
    
    ./rsocket-cli --help
    Usage: rsocket-cli [-hV] [--channel] [--debug] [--fnf] [--metadataPush]
                       [--request] [--resume] [--stream] [--complete=<complete>]
                       [--dataFormat=<dataFormat>] [-i=<input>]
                       [--keepalive=<keepalive>] [-m=<metadata>]
                       [--metadataFormat=<metadataFormat>] [-r=<requestN>]
                       [--route=<route>] [-s=<setup>] [--timeout=<timeout>]
                       [-H=<headers>]... [target]
    RSocket CLI command
          [target]              Endpoint URL
          --channel             Channel
          --complete=<complete> Complete Argument
          --dataFormat=<dataFormat>
                                Data Format
          --debug               Debug Output
          --fnf                 Fire and Forget
      -h, --help                Show this help message and exit.
      -H, --header=<headers>    Custom header to pass to server
      -i, --input=<input>       String input or @path/to/file
          --keepalive=<keepalive>
                                Keepalive period
      -m, --metadata=<metadata> Metadata input string input or @path/to/file
          --metadataFormat=<metadataFormat>
                                Metadata Format
          --metadataPush        Metadata Push
      -r, --requestn=<requestN> Request N credits
          --request             Request Response
          --resume              resume enabled
          --route=<route>       RSocket Route
      -s, --setup=<setup>       String input or @path/to/file for setup metadata
          --stream              Request Stream
          --timeout=<timeout>   Timeout in seconds
      -V, --version             Print version information and exit.
    ```

## Naming Server and Service Registry

![](docs/resources/images/eureka.png)

## H2 DB in your `jdbc:h2:~/test`

* user: `sa`
* password: _empty_

![h2](docs/resources/h2.png)

## Diagram Tools

https://docs.asciidoctor.org/diagram-extension/latest/

### `ditaa` ascii diagram
* https://asciiflow.com
* https://textik.com/


```ditaa
+--------+   +-------+    +-------+
|        | --+ ditaa +--> |       |
|  Text  |   +-------+    |diagram|
|Document|   |!magic!|    |       |
|     {d}|   |       |    |       |
+---+----+   +-------+    +-------+
:                         ^
|       Lots of work      |
+-------------------------+
```

### `Plant UML`
* You may encounter issue that `/opt/local/bin/dot` not found.
* Follow installation at [this link](https://plantuml.com/en/graphviz-dot)
* Create symbolic link
    ```shell
    sudo ln -s /opt/homebrew/Cellar/graphviz/8.0.5/bin/dot /opt/local/bin/dot
    ```
    ```plantuml
    @startuml
    package "Some Group" {
      HTTP - [First Component]
      [Another Component]
    }
    
    node "Other Groups" {
      FTP - [Second Component]
      [First Component] --> FTP
    }
    
    cloud {
      [Example 1]
    }
    
    
    database "MySql" {
      folder "This is my folder" {
        [Folder 3]
      }
      frame "Foo" {
        [Frame 4]
      }
    }
    
    
    [Another Component] --> [Example 1]
    [Example 1] --> [Folder 3]
    [Folder 3] --> [Frame 4]
    @enduml
    ```

### `Mermaid`

```mermaid
graph TD
    A[Christmas] -->|Get money| B(Go shopping)
    B --> C{Let me think}
    C -->|One| D[Laptop]
    C -->|Two| E[iPhone]
    C -->|Three| F[fa:fa-car Car]
```

![image](docs/resources/images/asciidoctor-diagram.png)
