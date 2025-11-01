$LOG_COMPILE = "maven.log"
$LOG_DEPS = "maven_dep.log"

Start-Process "powershell" -ArgumentList "-Command", "mvn clean compile *>>'$LOG_COMPILE'" -NoNewWindow -PassThru | ForEach-Object { $PID_COMPILE = $_.Id }
Start-Process "powershell" -ArgumentList "-Command", "mvn dependency:copy-dependencies *>>'$LOG_DEPS'" -NoNewWindow -PassThru | ForEach-Object { $PID_DEPS = $_.Id }

Wait-Process -Id $PID_COMPILE
Wait-Process -Id $PID_DEPS

$CLASE_PRINCIPAL = "com.library.kernel.jartisan.src.Jartisan"
$CLASSPATH = ".\target\classes;.\target\dependency\*"

java -cp "$CLASSPATH" "$CLASE_PRINCIPAL" @args