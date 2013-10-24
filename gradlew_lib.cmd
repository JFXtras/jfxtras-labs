@echo off
IF EXIST "gradle_localOverride.cmd" call "gradle_localOverride.cmd"
del /F /Q _lib\*
call gradlew copyToLib
pause