@ECHO OFF
echo kafka url %1
java -jar trading-streaming-consumer-1.0-SNAPSHOT.jar --kafka.host=%1
