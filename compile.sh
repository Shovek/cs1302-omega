javac -d bin -p $JAVAFX_HOME/lib --add-modules javafx.controls pong/Pong.java
java -cp bin -Dprism.order=sw -p $JAVAFX_HOME/lib --add-modules javafx.controls pong.Pong
