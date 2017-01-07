@echo off
IF EXIST "gradle_localOverride.cmd" call "gradle_localOverride.cmd"
call gradlew clean shadowJar install -x test -x signArchives -x javadoc
pause