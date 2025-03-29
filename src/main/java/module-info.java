module dragonUI {

    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.media;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    opens dragonUI to javafx.fxml;

    exports dragonUI;
    exports dragon;

    opens dragon to javafx.fxml;
}
