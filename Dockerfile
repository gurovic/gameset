# AlpineLinux with a glibc-2.23, Oracle Java 8, sbt and git
FROM anapsix/alpine-java:8_jdk

# sbt

ENV SBT_VERSION 1.8.2
ENV SBT_HOME /opt/sbt
ENV PATH ${PATH}:${SBT_HOME}/sbt/bin

# Install sbt
RUN apk add --no-cache --update bash wget && mkdir -p "$SBT_HOME" && \
    wget -qO - --no-check-certificate "https://github.com/sbt/sbt/releases/download/v$SBT_VERSION/sbt-$SBT_VERSION.tgz" |  tar xz -C $SBT_HOME && \
    echo -ne "- with sbt $SBT_VERSION\n" >> /root/.built

# Copy play project and compile it

ENV PROJECT_HOME /opt/gameset

COPY . ${PROJECT_HOME}
RUN cd $PROJECT_HOME/gameset && \
    sbt compile

# Command

CMD sbt run -Dslick.dbs.default.db.properties.url=$DATABASE_URI


# Expose code volume and play port 9000 

EXPOSE 9000
WORKDIR $PROJECT_HOME/gameset

# EOF
