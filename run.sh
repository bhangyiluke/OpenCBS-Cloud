mvn -f server/opencbs-server clean spring-boot:run -DBUILD_VERSION=1.0.1
# -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"