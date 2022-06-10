module pmf.projekatrm {
    requires javafx.controls;
    requires javafx.fxml;


    opens pmf.projekatrm to javafx.fxml;
    exports pmf.projekatrm;
}