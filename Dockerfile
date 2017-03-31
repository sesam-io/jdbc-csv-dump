FROM java:8-jre-alpine

COPY entrypoint.sh /
ENTRYPOINT ["/entrypoint.sh"]

ADD target/jdbc-csv-dump-1.0-SNAPSHOT.jar /srv/
COPY target/lib /srv/lib

