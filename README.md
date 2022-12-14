# MyKogitoDmTestCounters Project

## Notes

```
GROUP_ID=marco.demos.kogito
APP_NAME=MyKogitoDmTestCounters

RH_QM_PLUGIN_VER="2.7.6.Final-redhat-00009"
PRJ_GROUP=marco.demos.kogito
APP_NAME=MyKogitoDmTestCounters

mvn com.redhat.quarkus.platform:quarkus-maven-plugin:${RH_QM_PLUGIN_VER}:create \
	-DprojectGroupId=${PRJ_GROUP} \
	-DprojectArtifactId=${APP_NAME} \
	-DplatformGroupId=com.redhat.quarkus.platform \
	-DplatformVersion=${RH_QM_PLUGIN_VER}

cd ${APP_NAME}
quarkus ext add org.kie.kogito:kogito-quarkus
quarkus ext add quarkus-resteasy-jackson
quarkus ext add io.quarkus:quarkus-smallrye-openapi
quarkus ext add io.quarkus:quarkus-smallrye-health

# in pom.xml
    <dependency>
      <groupId>org.kie.kogito</groupId>
      <artifactId>monitoring-prometheus-quarkus-addon</artifactId>
    </dependency>

# in application.properties
	quarkus.devservices.enabled=false
```

## OCP Prometheus

```
TNS=my-dm-counters
MONITORED_APP_NAME=my-kogito-dm-test-counters
SM_NAME=monitor-${MONITORED_APP_NAME}


cat <<EOF | oc apply -f -
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor
metadata:
  labels:
    app: ${SM_NAME}
  name: ${SM_NAME}
  namespace: ${TNS}
spec:
  endpoints:
  - interval: 15s
    port: http
    targetPort: 8080
    path: /q/metrics
    scheme: http
  namespaceSelector:
    matchNames:
    - ${TNS}
  selector:
    matchLabels:
      app: ${MONITORED_APP_NAME}
EOF


cat <<EOF | oc apply -f -
apiVersion: monitoring.coreos.com/v1
kind: Prometheus
metadata:
  name: prometheus
  namespace: ${TNS}
spec:
  serviceAccountName: prometheus
  serviceMonitorSelector:
    matchLabels:
      app: ${MONITORED_APP_NAME}
EOF
```

## Payload samples

### FlightRebooking

```
{
  "Flight List": [
    {
      "Flight Number": "100",
      "From": "Roma",
      "To": "Nowhere",
      "Departure": "2022-01-01T07:00:00.000Z",
      "Arrival": "2222-01-01T07:00:00.000Z",
      "Capacity": 10,
      "Status": "cancelled"
    },
    {
      "Flight Number": "101",
      "From": "Roma",
      "To": "Nowhere",
      "Departure": "2023-01-01T07:00:00.000Z",
      "Arrival": "2223-01-01T07:00:00.000Z",
      "Capacity": 8,
      "Status": "scheduled"
    },
    {
      "Flight Number": "102",
      "From": "Roma",
      "To": "Nowhere",
      "Departure": "2023-02-01T07:00:00.000Z",
      "Arrival": "2223-02-01T07:00:00.000Z",
      "Capacity": 5,
      "Status": "scheduled"
    }
		
  ],
  "Passenger List": [
    {
      "Name": "passenger1",
      "Status": "bronze",
      "Miles": 200,
      "Flight Number": "100"
    },
    {
      "Name": "passenger2",
      "Status": "bronze",
      "Miles": 80,
      "Flight Number": "100"
    },
    {
      "Name": "passenger3",
      "Status": "bronze",
      "Miles": 120,
      "Flight Number": "100"
    },
    {
      "Name": "passenger4",
      "Status": "bronze",
      "Miles": 160,
      "Flight Number": "100"
    },
    {
      "Name": "passenger5",
      "Status": "bronze",
      "Miles": 10,
      "Flight Number": "100"
    },
    {
      "Name": "passenger6",
      "Status": "bronze",
      "Miles": 300,
      "Flight Number": "100"
    },
    {
      "Name": "passenger7",
      "Status": "silver",
      "Miles": 550,
      "Flight Number": "100"
    },
    {
      "Name": "passenger8",
      "Status": "silver",
      "Miles": 600,
      "Flight Number": "100"
    },
    {
      "Name": "passenger9",
      "Status": "silver",
      "Miles": 500,
      "Flight Number": "100"
    },
    {
      "Name": "passenger10",
      "Status": "gold",
      "Miles": 1000,
      "Flight Number": "100"
    }
		
  ]
}
```

### Example of Prometheus metrics


#### quantiles

```
api_execution_elapsed_seconds{artifactId="MyKogitoDmTestCounters"}
```

#### rules executed in time frame

```
increase(api_http_response_code_total{artifactId="MyKogitoDmTestCounters"}[1m])
```



# Useful
This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it???s not an _??ber-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _??ber-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _??ber-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/MyKogitoDmTestCounters-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
