all : Gange

Gange : 
	javac -d bin/ -classpath bin/ojdbc6.jar -sourcepath src/ src/test/TestGange.java
	java -classpath bin:bin/ojdbc6.jar test/TestGange

clean : 
	rm -f bin/test/*.class
	rm -f bin/window/*.class
