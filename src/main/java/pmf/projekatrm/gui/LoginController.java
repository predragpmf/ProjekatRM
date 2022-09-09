package pmf.projekatrm.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import pmf.projekatrm.connection.KlijentUDP;
import pmf.projekatrm.connection.ServerUDP;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController implements Initializable {

    public static ServerUDP udpServer;

    public static KlijentUDP udpKlijent;

    @FXML
    private TextField loginInputField;
    @FXML
    private Button loginButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prijava();
    }

    // Pritisak "Login" tipke:
    private void prijava() {
        loginButton.setOnAction(event -> {
            String korisnickoIme;
            korisnickoIme = loginInputField.getText();
            if (korisnickoIme.length() < 4 || korisnickoIme.length() > 12) {
                obavjestenjeProzor("Greška!", "Pogrešan unos", "Korisničko ime mora biti između 4 i 12 znaka.");
                loginInputField.clear();
                return;
            }
            if (!provjeraIspravnosti(korisnickoIme)) {
                obavjestenjeProzor("Greška!", "Pogrešan unos", "Dozvoljena su samo slova alfabeta i brojevi.");
                loginInputField.clear();
                return;
            } else {
                ServerUDP.prijavljeniIgrac = korisnickoIme;
                KlijentUDP.poruka = korisnickoIme;
                String[] args = {};
                udpServer = new ServerUDP();
                udpServer.start();
                udpKlijent = new KlijentUDP();
                udpKlijent.start();
                Window.promjeniScenu("TrazenjeIgraca.fxml", "Traženje igrača", 800, 600);
            }
        });
    }

    // Prihvata samo brojeve, mala i velika slova:
    private boolean provjeraIspravnosti(String tekst) {
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tekst);
        return matcher.matches();
    }

    // Prikazuje prozor sa obavjestenjem:
    private void obavjestenjeProzor(String naslov, String zaglavlje, String sadrzaj) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(naslov);
        alert.setHeaderText(zaglavlje);
        alert.setContentText(sadrzaj);
        alert.showAndWait();
    }
}
