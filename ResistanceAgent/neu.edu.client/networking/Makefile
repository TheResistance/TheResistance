JFLAGS = -d ../
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java


default: classes

classes: *.class

clean:
	$(RM) *.class

run:
	cd ../ && java networking.ClientMain
