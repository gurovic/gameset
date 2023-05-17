FROM openjdk:17-bullseye

ENV PROJECT_HOME /opt/gameset

ENV SBT_VERSION 1.8.2
ENV SBT_HOME /opt/sbt
ENV PATH ${PATH}:${SBT_HOME}/sbt/bin

WORKDIR $PROJECT_HOME

RUN mkdir -p "$SBT_HOME" && \
    wget -qO - --no-check-certificate "https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz" |  tar xz -C $SBT_HOME && \
    echo -ne "- with sbt $SBT_VERSION\n" >> /root/.built

COPY gameset ${PROJECT_HOME}
RUN cd $PROJECT_HOME && \
    sbt compile

EXPOSE 9000

CMD sbt run -Dslick.dbs.default.db.properties.url=$DATABASE_URI
