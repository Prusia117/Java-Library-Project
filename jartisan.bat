@echo off
setlocal

set LOG_COMPILE=maven.log
set LOG_DEPS=maven_dep.log

rem Ejecutar en la misma ventana (sin abrir nuevas)
call mvn clean compile >> "%LOG_COMPILE%" 2>&1
call mvn dependency:copy-dependencies >> "%LOG_DEPS%" 2>&1

rem Esperar 20 segundos (ajústalo según tu proyecto)
timeout /t 20 >nul

set "CLASE_PRINCIPAL=com.library.kernel.jartisan.src.Jartisan"
set "CLASSPATH=.\target\classes;.\target\dependency\*"

java -cp "%CLASSPATH%" %CLASE_PRINCIPAL% %*

endlocal
