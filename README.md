# trading-report

These steps have to be run in the precise order mentioned below. It has strong dependency on each other.
1. Start the zookeeper
2. Start kafka server
3. Create the following topics 
trading-records
trading-report

For unix:
kafka-topics.sh --create --bootstrap-server <kafka_host:port> --replication-factor 1 --partitions 1 --topic trading-records
kafka-topics.sh --create --bootstrap-server <kafka_host:port> --replication-factor 1 --partitions 1 --topic trading-report

For windows:
bin/kafka-topics.bat --create --bootstrap-server <kafka_host:port> --replication-factor 1 --partitions 1 --topic trading-records
bin/kafka-topics.bat --create --bootstrap-server <kafka_host:port> --replication-factor 1 --partitions 1 --topic trading-report

4. Download ALL the files in the  dist/  folder in any of your work directory.
5. Send the records from file Input.txt 
kafka-console-producer.sh --broker-list <kafka_host:port> --topic trading-records < Input.txt

5. In order to start the kafka-consumer application run below commands:
For unix:
sh run.sh <kafka_host:port>
e.g. 
sh run.sh localhost:9092

For windows
run.bat <kafka_host:port>
e.g.
run.bat localhost:9092

7. Launch the app:
For json version:
http://localhost:8080/report/json

For downloading csv:
http://localhost:8080/report/csv
