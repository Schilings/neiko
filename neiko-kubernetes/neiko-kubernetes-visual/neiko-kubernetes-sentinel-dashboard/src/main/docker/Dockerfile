ARG VERSION=1.8-full
FROM moxm/java:$VERSION
RUN mkdir -p /neiko-kubernetes-sentinel-dashboard
WORKDIR /deployments
ARG JAR_FILE=maven/target/neiko-kubernetes-sentinel-dashboard.jar
COPY ${JAR_FILE} app.jar
EXPOSE 5003
ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"
CMD sleep 60; java -jar app.jar $JAVA_OPTS
