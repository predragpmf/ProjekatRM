module pmf.projekatrm {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;


    opens pmf.projekatrm.gui to javafx.fxml, javafx.controls, javafx.graphics, javafx.base;
    exports pmf.projekatrm;
    opens pmf.projekatrm.game to javafx.base, javafx.controls, javafx.fxml, javafx.graphics;
}