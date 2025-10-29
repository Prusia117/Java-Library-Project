#!/bin/bash
CLASE_PRINCIPAL="com.library.kernel.jartisan.src.Jartisan"
CLASSPATH="./target/classes"
java -cp "${CLASSPATH}" "${CLASE_PRINCIPAL}" "$@"
