CREATE KEYSPACE relay42 WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3};


CREATE TABLE relay42.sensor_data (
  sensorId varchar,
  readingValue double,
  readingDate timestamp,
  PRIMARY KEY (sensorId, readingDate));