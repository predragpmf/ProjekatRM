package pmf.projekatrm.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pmf.projekatrm.game.Igrac;
import pmf.projekatrm.game.KlijentTCP;
import pmf.projekatrm.game.KlijentUDP;
import pmf.projekatrm.game.ServerUDP;

import java.net.URL;
import java.util.ResourceBundle;

public class TrazenjeIgracaController implements Initializable {

    public static Igrac ig;
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
        posaljiZahtjev();

    }

    private void osvjeziIgrace() {
        osvjeziIgraceButton.setOnAction(event -> {
            korisnickoImeColumn.setCellValueFactory(new PropertyValueFactory<Igrac, String>("korisnickoIme"));
            stanjeIgracaColumn.setCellValueFactory(new PropertyValueFactory<Igrac, String>("stanje"));
            sviIgraciTable.setItems(getIgraci());
        });
    }

    private void posaljiZahtjev() {
        posaljiZahtjevButton.setOnAction(event -> {

            ig = sviIgraciTable.getSelectionModel().getSelectedItem();
            KlijentUDP.poruka = "connect:" + ig.getKorisnickoIme();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int port = Integer.parseInt(ServerUDP.protivnik.split(":")[1]);
            System.out.println("Port primljenog TCP servera je: " + port);
            ServerUDP.running = false;
            KlijentUDP.poruka = "busy:" + port + ":" + ServerUDP.prijavljeniIgrac;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String[] args = {};
            KlijentTCP.port = port;
            KlijentTCP.main(args);
            Window.promjeniScenu("Igra.fxml", "Igra", 800, 600);

        });
    }

    public ObservableList<Igrac> getIgraci() {
        ObservableList<Igrac> igraci = FXCollections.observableArrayList();
        igraci.addAll(Igrac.sviIgraci);
        return igraci;
    }


}
