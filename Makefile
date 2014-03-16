JC=javac
JFLAGS=

.SUFFIXES: .java .class

CLASSES =\
    ServerMain.java \
    ClientServerSide.java
 
.java.class:
	$(JC) $(JFLAGS) $*.java

default: classes 

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
