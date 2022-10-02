module com.multi_project.hanging_man {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.logging;
    requires java.json;

    opens com.multi_project.hanging_man to javafx.fxml;
    exports com.multi_project.hanging_man.exceptions;
    opens com.multi_project.hanging_man.exceptions to javafx.fxml;
    exports com.multi_project.hanging_man.screens;
    opens com.multi_project.hanging_man.screens to javafx.fxml;
    exports com.multi_project.hanging_man.controllers;
    opens com.multi_project.hanging_man.controllers to javafx.fxml;
    exports com.multi_project.hanging_man.utils;
    opens com.multi_project.hanging_man.utils to javafx.fxml;
    exports com.multi_project.hanging_man;
}