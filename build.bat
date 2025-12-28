@echo off
set CLASSPATH=.;lib\sqlite-jdbc.jar
javac -cp %CLASSPATH% *.java
