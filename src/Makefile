JC=javac
JFLAGS=
DEST=-d ../bin/
.SUFFIXES: .java .class

CLASSES =\
    ClientServerSide.java \
    ServerMain.java
 
.java.class:
	$(JC) $(JFLAGS) $(DEST) $*.java

default: classes 

classes: $(CLASSES:.java=.class)

clean:
	$(RM) ../bin/*.class
