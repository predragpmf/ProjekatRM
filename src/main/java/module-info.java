module pmf.projekatrm {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens pmf.projekatrm.gui to javafx.fxml, javafx.controls, javafx.graphics;
    exports pmf.projekatrm;
}