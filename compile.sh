javac -d bin -p $JAVAFX_HOME/lib --add-modules javafx.controls src/main/java/cs1302/omega/OmegaApp.java
java -cp bin -Dprism.order=sw -p $JAVAFX_HOME/lib --add-modules javafx.controls cs1302.omega.OmegaApp
