@echo off
call preprocess.bat
set "JAVA_HOME=F:/My Programs Expansion/Java/OpenJDK17U-jdk_x64_windows_hotspot_17.0.3_7/jdk-17.0.3+7"
REM set "JAVA_HOME=F:/My Programs Expansion/Java/jdk-17.0.5_windows-x64_bin/jdk-17.0.5"
REM "%JAVA_HOME%/bin/java" -XX:+UnlockDiagnosticVMOptions -XX:+PrintFlagsFinal -version>java_flags.txt
::set JDK_JAVA_OPTIONS 
gradlew.bat runClient