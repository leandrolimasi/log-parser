LOG PARSER
==============================================================================================

Author: Leandro Lima

Technologies: Sprint Boot


Development requirements
-------------------

Java 8 Jdk or newer. 

Maven 3.x 


BUILD 
-------------------------

    $ mvn clean install

RUN 
-------------------------

    $ java -jar ${PROJECT_PATH}/target/log-parser-0.0.1-SNAPSHOT.jar --startDate=2017-11-01.00:30:00  --duration=hourly --threshold=2 --logfile=${LOG_PATH}


