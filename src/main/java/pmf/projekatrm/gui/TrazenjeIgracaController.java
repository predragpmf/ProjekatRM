package pmf.projekatrm.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pmf.projekatrm.game.Igrac;
import pmf.projekatrm.game.KlijentTCP;
import pmf.projekatrm.game.KlijentUDP;
import pmf.projekatrm.game.ServerUDP;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class TrazenjeIgracaController implements Initializable {

    public static volatile boolean klijent = false;
    public static volatile Igrac ig;
    public static boolean odgovor;
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

    // TODO
    // Zahtjevi za igru:
    //
    public static boolean showDialog() {
        Platform.runLater(new Runnable() {
            public void run() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Look, a Confirmation Dialog");
                alert.setContentText("Are you ok with this?");

                Optional<ButtonType> result = alert.showAndWait();
                odgovor = result.get() == ButtonType.OK;
            }
        });
        return odgovor;
    }

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
            /*
            if (ig == null) {
                System.out.println("Igrac nije odabran");
                return;
            }
            */
            ig = sviIgraciTable.getSelectionModel().getSelectedItem();
            if (ig.getStanje().equals("zauzet")) {
                System.out.println("Igrac je zauzet");
                return;
            }
            KlijentUDP.poruka = "connect:" + ig.getKorisnickoIme();
            boolean cekanje = false;
            try {
                cekanje = ServerUDP.prihvacenZahtjev.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
            if (cekanje) {
                int port = Integer.parseInt(ServerUDP.protivnik.split(":")[1]);
                System.out.println("Port primljenog TCP servera je: " + port);
                ServerUDP.running = false;
                KlijentUDP.poruka = "busy:" + port + ":" + ServerUDP.prijavljeniIgrac;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                KlijentTCP.port = port;
                klijent = true;
                new KlijentTCP().start();
                Window.promjeniScenu("Igra.fxml", "Igra", 800, 600);
            } else {
                System.out.println("Connection timeout");
            }
        });
    }

    public ObservableList<Igrac> getIgraci() {
        ObservableList<Igrac> igraci = FXCollections.observableArrayList();
        igraci.addAll(Igrac.sviIgraci);
        return igraci;
    }

}
