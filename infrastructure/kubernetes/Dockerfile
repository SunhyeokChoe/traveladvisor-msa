FROM confluentinc/cp-kafka-connect:7.4.1

LABEL maintainer="sunhyeokchoe@gmail.com"

USER root
RUN yum install unzip -y
RUN mkdir -p /usr/share/java/
WORKDIR /

# debezium-connector-postgres
RUN wget https://repo1.maven.org/maven2/io/debezium/debezium-connector-postgres/3.0.4.Final/debezium-connector-postgres-3.0.4.Final-plugin.tar.gz -O /tmp/debezium-connector-postgres.tar.gz \
&& tar -xvzf /tmp/debezium-connector-postgres.tar.gz --directory /usr/share/java/ \
&& rm /tmp/debezium-connector-postgres.tar.gz
