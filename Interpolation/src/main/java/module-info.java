module com.artemis.interpolation {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.artemis.interpolation to javafx.fxml;
    exports com.artemis.interpolation;
}