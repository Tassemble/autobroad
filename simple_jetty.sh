#!/bin/sh
export MAVEN_OPTS="-XX:MaxPermSize=128M -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n $MAVEN_OPTS"

mvn jetty:run -Djetty.port=8080
