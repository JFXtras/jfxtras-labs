JFXtras-labs [![Build Status](https://travis-ci.org/JFXtras/jfxtras-labs.png)](https://travis-ci.org/JFXtras/jfxtras-labs)
============
_A project for experimenting with new controls and other useful JavaFX extensions_

Java version
--------------------
JavaFX 2.2 - JDK 7
```git checkout 2.2```

JavaFX 8 - JDK 8
```git checkout 8.0```

Install/Build
--------------------
Building with Gradle
Command ```./gradlew build``` will install necessary gradle version

```
git clone https://github.com/JFXtras/jfxtras-labs.git
cd jfxtras-labs
git checkout *YOUR_JAVAFX_VERSION*
./gradlew build
```

Troubleshooting
--------------------
set correct JAVA_HOME
example ```JAVA_HOME=/usr/lib/jvm/default-java/``` in .bashrc
test ```file $JAVA_HOME/jre/lib/jfxrt.jar``` Unix
sometimes ```jre/lib/jfxrt.jar``` placed in ```jre/lib/ext/jfxrt.jar``` you can make symlink


### Please make [Issue](https://github.com/JFXtras/jfxtras-labs/issues) if have problem with build (write linux/windows/OSX version, JDK version/build, log file with errors)

