call java_localOverride.cmd
set JAVA_OPTS="--add-exports javafx.graphics/com.sun.glass.ui=jfxtras.controls"
call mvnw clean test -DfailIfNoTests=false
pause