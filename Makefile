JCC=javac
JFLAGS=-g -Xlint -Xlint:-serial
SOURCEPATH=src
CLASSDIR=bin

poly-ml-monitor.tgz: java bin/polystats
	tar -czf poly-ml-monitor.tgz bin/ LICENSE poly-ml-monitor README.md

.PHONY: java

java:
	$(JCC) $(JFLAGS) -d $(CLASSDIR) -sourcepath $(SOURCEPATH) -classpath bin/jfreechart-1.0.14.jar:bin/jcommon-1.0.17.jar $(SOURCEPATH)/com/Main.java

bin/polystats: polystats.sml
	polyc polystats.sml -o bin/polystats
