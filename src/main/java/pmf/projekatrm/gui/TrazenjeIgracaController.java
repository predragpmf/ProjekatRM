package pmf.projekatrm.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TrazenjeIgracaController implements Initializable {

    @FXML
    private Button osvjeziIgraceButton;
    @FXML
    private Button posaljiZahtjevButton;
    @FXML
    private TableView<Igrac> sviIgraciTable;
    @FXML
    private TableColumn<Igrac, String> korisnickoImeColumn;
    @FXML
    private TableColumn<Igrac, String> stanjeIgracaColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        osvjeziIgrace();

    }

    private void osvjeziIgrace() {
        osvjeziIgraceButton.setOnAction(event -> {
            korisnickoImeColumn.setCellValueFactory(new PropertyValueFactory<Igrac, String>("korisnickoIme"));
            stanjeIgracaColumn.setCellValueFactory(new PropertyValueFactory<Igrac, String>("stanje"));
            sviIgraciTable.setItems(getIgraci());
        });
    }

    public ObservableList<Igrac> getIgraci() {
        ObservableList<Igrac> igraci = FXCollections.observableArrayList();
        igraci.addAll(Igrac.sviIgraci);
        return igraci;
    }

}
