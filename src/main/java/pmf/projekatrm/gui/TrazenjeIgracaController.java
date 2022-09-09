package pmf.projekatrm.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.StageStyle;
import pmf.projekatrm.connection.KlijentTCP;
import pmf.projekatrm.connection.KlijentUDP;
import pmf.projekatrm.connection.ServerUDP;
import pmf.projekatrm.game.Igrac;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class TrazenjeIgracaController implements Initializable {

    public static volatile boolean klijent = false;
    public static volatile Igrac ig;
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

    // Pritisak tipke "Osvjezi":
    private void osvjeziIgrace() {
        osvjeziIgraceButton.setOnAction(event -> {
            sviIgraciTable.getItems().clear();
            korisnickoImeColumn.setCellValueFactory(new PropertyValueFactory<Igrac, String>("korisnickoIme"));
            stanjeIgracaColumn.setCellValueFactory(new PropertyValueFactory<Igrac, String>("stanje"));
            sviIgraciTable.setItems(getIgraci());
        });
    }

    // Pritisak tipke "Posalji":
    private void posaljiZahtjev() {
        posaljiZahtjevButton.setOnAction(event -> {
            new Thread(new Runnable() {
                public void run() {

                    ig = sviIgraciTable.getSelectionModel().getSelectedItem();
                    if (ig.getStanje().equals("zauzet")) {
                        System.out.println("Igrac je zauzet");
                        return;
                    }
                    KlijentUDP.poruka = "connect:" + ig.getKorisnickoIme();
                    boolean cekanje = false;
                    try {
                        cekanje = ServerUDP.prihvacenZahtjev.await(15, TimeUnit.SECONDS);
                    } catch (InterruptedException exc) {
                        exc.printStackTrace();
                    }
                    if (cekanje) {
                        int port = Integer.parseInt(ServerUDP.protivnik.split(":")[1]);
                        System.out.println("Port primljenog TCP servera je: " + port);
                        KlijentUDP.poruka = "busy:" + port + ":" + ServerUDP.prijavljeniIgrac;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        KlijentTCP.port = port;
                        klijent = true;
                        new KlijentTCP().start();
                        ServerUDP.running = false;
                        ig = null;
                        sviIgraciTable.getSelectionModel().clearSelection();
                        Window.promjeniScenu("Igra.fxml", "Igra", 800, 600);
                    } else {
                        System.out.println("Connection timeout");
                        prikaziPoruku();
                    }

                }
            }).start();

        });
    }

    // Prikazuje prozor sa obavjestenjem:
    private void prikaziPoruku() {
        Platform.runLater(new Runnable() {
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Greska!");
                alert.setHeaderText(null);
                alert.setContentText("Protivnik nije dostupan!");
                alert.showAndWait();
            }
        });
    }

    // Kreira listu igraca, koja se ubacuje u tabelu:
    public ObservableList<Igrac> getIgraci() {
        ObservableList<Igrac> igraci = FXCollections.observableArrayList();
        igraci.addAll(Igrac.sviIgraci);
        return igraci;
    }

}
