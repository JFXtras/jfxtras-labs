@echo off
IF EXIST "gradle_localOverride.cmd" call "gradle_localOverride.cmd"
call gradlew dependencies
del /F /Q _lib\*
call gradlew copyToLib
pause