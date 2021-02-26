/**
 * The project module
 * Open is needed to ensure that JavaFX can correctly access our classes
 *
 * Note: In the latest IntelliJ version there may be a warning for the module:
 * "Module 'it.polimi.ingsw' reads package 'javafx.animation' from both 'javafx.graphics' and 'javafx.graphics'"
 * This warning is caused by the ide ignoring the &lt;classifier&gt; parameter in org.openjfx:javafx-graphics dependencies
 * that enables Maven to package the correct files for each OS in the final JAR, instead incorrectly assuming that the
 * same classes are being imported multiple times.
 */
open module it.polimi.ingsw {
    requires java.naming;
    requires javafx.controls;
    requires com.google.gson;
}