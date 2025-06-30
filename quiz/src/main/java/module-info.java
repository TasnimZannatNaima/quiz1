module org.example.quiz {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires javafx.swing;

    opens ui to javafx.fxml;
    exports ui;
    exports mainapp;
    opens mainapp to javafx.fxml;
}