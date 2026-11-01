#!/bin/bash
LOG_COMPILE="maven.log"
LOG_DEPS="maven_dep.log"
./mvnw clean compile >> "$LOG_COMPILE" 2>&1 &
PID_COMPILE=$!
./mvnw dependency:copy-dependencies >> "$LOG_DEPS" 2>&1 &
PID_DEPS=$!

wait $PID_COMPILE
wait $PID_DEPS

CLASE_PRINCIPAL="com.library.kernel.jartisan.src.Jartisan"
CLASSPATH="./target/classes:./target/dependency/*"

# Ejecuta con las dependencias incluidas
java -cp "${CLASSPATH}" "${CLASE_PRINCIPAL}" "$@"
