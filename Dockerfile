FROM websphere-liberty:webProfile7
MAINTAINER IBM Java engineering at IBM Cloud
ARG servername=asistentevirtual-mysql
ARG port=3306
ARG user=root
ARG pass=password
ARG database=watsonws
ENV servername=$servername
ENV port=$port
ENV user=$user
ENV pass=$pass
ENV database=$database
COPY /target/liberty/wlp/usr/servers/defaultServer /config/
COPY /target/liberty/wlp/usr/shared/resources /config/resources/
COPY /src/main/liberty/config/jvmbx.options /config/jvm.options
RUN installUtility install --acceptLicense defaultServer
# Upgrade to production license if URL to JAR provided
ARG LICENSE_JAR_URL
RUN \
  if [ $LICENSE_JAR_URL ]; then \
    wget $LICENSE_JAR_URL -O /tmp/license.jar \
    && java -jar /tmp/license.jar -acceptLicense /opt/ibm \
    && rm /tmp/license.jar; \
  fi
