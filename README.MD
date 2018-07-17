

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


### Build and Run the sensor simulator
Let's now build the sensor-simulator project.
	

	cd $PROJECT_ROOT/sensor-simulator

    mvn clean compile assembly:single
    

In order to start the sensor simulator 
	
	java -jar target/sensor-simulator-1.0-SNAPSHOT-jar-with-dependencies.jar config.properties

The config.properties should define the following configurations (An example was added in the $PROJECT_ROOT).
    
    - topic.name: The Kafka topic name 

	- bootstrap.servers  A comma separated list of servers:ports

	- sensor.id The id of the sensor, it will be used to identify the simulated sensor

If you to simulate multiple sensors change the sensor.id configuration and run the command again.

Now let's build the consumer project.

	cd $PROJECT_ROOT/consumer
 	 
 	mvn clean compile assembly:single

And start it using:

	java -jar target/consumer-1.0-SNAPSHOT-jar-with-dependencies.jar


After starting sensor simulator, simulated sensor data will be generated and sent to the specified kafka topic. The consumer will receive the data and add it to cassandra.

Finally let's build and run the web-service project.
	
	cd $PROJECT_ROOT/web-service
	
	mvn jetty:run

The above command will compile and deploy the web-service project.

In order to obtain the sensor stats (average//max/min/count values) you can execute the following curl. 
Note you will need to update start/end values with an time interval used to generate sensors reading by the sensor-simulator.


	curl -X POST -H "Content-Type: application/json" -d '{"sensorId":"sen1","start":"2018-07-16T23:14:18.000000Z", "end":"2018-07-16T23:15:39.486000Z"}' "http:/localhost:8080/web-service/services/sensor/"
    
The response should look like something like

	{"average":"0.49230806972078944","count":"79.0","max":"0.9968394599415784","min":"0.006311711200555514","sum":"38.892337507942365"}

If there is not sensor data for the provided data the service will return 204 no content.



