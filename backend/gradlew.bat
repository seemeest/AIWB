@echo off
setlocal

set DIR=%~dp0
set CLASSPATH=%DIR%gradle\wrapper\gradle-wrapper.jar;%DIR%gradle\wrapper\gradle-wrapper-shared.jar

if not "%JAVA_HOME%"=="" (
  set JAVA_EXEC=%JAVA_HOME%\bin\java.exe
) else (
  set JAVA_EXEC=java.exe
)

"%JAVA_EXEC%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

endlocal
