
Description
======================
This repository contains the implementation of IoT pipeline for processing sensor data. The architecture utilizes [Apache Kafka](https://kafka.apache.org) as message broker and [Apache Cassandra](http://cassandra.apache.org) as database. The solution presented here is composed by 4 components:

	- data-access: Common library for storing shared classes among the applications.
	- sensor-simulator: A daemon responsible for generating sensor readings and publishing it to a kafka topic.
	- consumer: Daemon for consuming the sensor readings and storing into cassandra keyspace.
	- web-service: Exposes a REST service for querying the sensor readings statistics (average,sum,mix,max adnd count).
	

Dependencies
======================
In order to run the projects the following dependencies are required.

* [Apache Kafka](https://kafka.apache.org/downloads) - A Distributed Streaming Platform
* [Apache Cassandra](http://cassandra.apache.org/download/) - NoSQL database management system designed to handle large amounts of data across many commodity servers


Build/Run Instructions
======================

After installing both kafka and cassandra let's now create the keyspace and column family on cassandra.

### Setuping Cassandra
First start cassandra:
	
	sh $CASSANDRA_HOME/bin/cassandra

Execute the CQL script to 
	
	sh $CASSANDRA_HOME/bin/cqlsh -f $PROJECT_ROOT/data-access/src/cassandra/exec.cql 

### Setuping Kafka
Now create the kafka topic 
	
	sh $KAFKA_HOME/bin/kafka-topics --zookeeper 127.0.0.1:2181 --create --topic iot_reads --partitions 5 --replication-factor 1

The partitions parameter it is an important configuration which defines the level of parallelism allowed for the topic. For instance, if the partitions value is 5
it will be possible to consume sensor readings using 5 consumers in parallel.
The topic parameter is the topic name and should match with the topic name used by the sensor-simulator and the consumer.

###Build the data-access library
	
	cd $PROJECT_ROOT/data-access

	mvn clean install

### Build and Run the sensor simulator
Let's now build the sensor-simulator

	cd $PROJECT_ROOT/sensor-simulator

    mvn clean compile assembly:single
    
In order to start the sensor simulator 
	
	java -jar target/sensor-simulator-1.0-SNAPSHOT-jar-with-dependencies.jar config.properties

The config.properties should define the following configurations (An example was added in the $PROJECT_ROOT).
    
    - topic.name: The kafka topic name.

	- bootstrap.servers: Comma separated list of kafka brokers.

	- sensor.id: The ids of the sensors you want to simulate. The default value is sen1,sen2,sen3 that will generate 3 sensors readings. If you want more sensors you can add more here. This ids should be used for querying the data through the web-service.

You can leave it running for a time and kill it using Ctrl + Z when you think that enough data was generated.

### Build and Run consumer project
Now let's build the consumer project.	

	cd $PROJECT_ROOT/consumer
 	 
 	mvn clean compile assembly:single

And start it using:

	java -jar target/consumer-1.0-SNAPSHOT-jar-with-dependencies.jar config.properties.

The config.properties has the following properties:

	- sensor.data.topic: The kafka topic name

	- bootstrap.servers: Comma separated list of kafka brokers

    - group.id: Identifies the group of consumer processes to which this consumer belongs. The broker will load balance between consumers of the same group.

    - cassandra.servers: The cassandra clusters list of contact points

    - cassandra.keyspace: The cassandra keyspace that was created by running the exec.cql script


After starting sensor simulator, simulated readings will be generated and sent to the specified kafka topic. The consumer will receive the data and send it to cassandra.
Multiple consumers can be run in parallel running the command again and respecting the number of partitions defined in the topic creation.


### Build and Run the web-service project
Finally let's build and run the web-service project.
	
	cd $PROJECT_ROOT/web-service
	
	mvn jetty:run

The above command will compile and deploy the web-service project.

In order to obtain the sensor stats (average//max/min/count values) you can execute the following curl. 
The web-service as enabled with a basic authorization using shiro. The allowed users can be found in [shiro.ini](web-service/src/main/resources/shiro.ini)

	curl -X POST -i --user relay42:secret -H "Content-Type: application/json" -d '{"sensorId":"sen1","start":"2018-07-17T00:52:28.000000Z", "end":"2018-07-17T00:52:56.000000Z"}' "http:/localhost:8080/web-service/services/sensor/"


Note you will need to change start/end values with an time interval that matches with the generated sensor readings.

    
The response body should look like the below json.

	{"average":"0.49230806972078944","count":"79.0","max":"0.9968394599415784","min":"0.006311711200555514","sum":"38.892337507942365"}

If there is no sensor data for the provided sensorId and interval dates the service will return 204 no content.
If the --user parameter is not allowed to access the resource it will return 401 unauthorized.


Limitations
======================
The implementation uses a json serializer and deserializer a more efficient approach would be using [Apache avro](https://avro.apache.org/) for serialization/deserialization would be better option while handling bigger payloads.

The consumer application could make use of the Kafka Streams library in order to calculate the statistics (average,min,max,sum). But, when querying the data one wouldn't
be able to pass any time interval because the data would be stored based on the defined time window on the stream agregator function e.g: daily,weekly.

The applications does not communicate using SSL.

The consumer could start multiple threads to consume the topic. The way that was implemented it is necessary to manually start another instance in order to get more consumers consuming the topic.

The web-services authentication relies on the [shiro.ini](web-service/src/main/resources/shiro.ini) file.

Lack of unit testing.






