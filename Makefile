JCC=javac
JFLAGS=-g -Xlint -Xlint:-serial
SOURCEPATH=src
CLASSDIR=bin

.PHONY: default java

default: poly-ml-monitor.jar poly-ml-monitor.tgz

poly-ml-monitor.jar: java
	jar cvf poly-ml-monitor.jar bin/ LICENSE poly-ml-monitor README.md

poly-ml-monitor.tgz: java
	tar -czf poly-ml-monitor.tgz bin/ LICENSE poly-ml-monitor README.md

java:
	$(JCC) $(JFLAGS) -d $(CLASSDIR) -sourcepath $(SOURCEPATH) -classpath bin/jfreechart-1.0.14.jar:bin/jcommon-1.0.17.jar $(SOURCEPATH)/com/Main.java
