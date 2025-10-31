#!/bin/bash
LOG_FILE="maven.log"
mvn clean compile >> "$LOG_FILE" 2>&1 &
PID_COMPILE=$!
mvn dependency:copy-dependencies >> "$LOG_FILE" 2>&1 &
PID_DEPS=$!

wait $PID_COMPILE
wait $PID_DEPS

CLASE_PRINCIPAL="com.library.kernel.jartisan.src.Jartisan"
CLASSPATH="./target/classes:./target/dependency/*"

# Ejecuta con las dependencias incluidas
java -cp "${CLASSPATH}" "${CLASE_PRINCIPAL}" "$@"
