@echo off
call preprocess.bat
REM set "JAVA_HOME=E:/java/graalvm-ce-java17-22.1.0"
set "JAVA_HOME=F:/My Programs Expansion/Java/OpenJDK17U-jdk_x64_windows_hotspot_17.0.3_7/jdk-17.0.3+7"
gradlew.bat build