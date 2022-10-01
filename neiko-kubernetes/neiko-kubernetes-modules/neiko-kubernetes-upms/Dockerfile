ARG VERSION=1.8-full
FROM moxm/java:$VERSION
RUN mkdir -p /deployments
WORKDIR /deployments
ARG JAR_FILE=maven/target/neiko-kubernetes-upms.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8081
ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"
CMD sleep 60; java -jar app.jar $JAVA_OPTS
