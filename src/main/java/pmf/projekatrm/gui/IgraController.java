package pmf.projekatrm.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import pmf.projekatrm.game.KlijentTCP;
import pmf.projekatrm.game.ServerTCP;
import pmf.projekatrm.game.ServerUDP;

import java.net.URL;
import java.util.ResourceBundle;

public class IgraController implements Initializable {

    public static String poruka;

    @FXML
    private TextField chatInput;
    @FXML
    private TextArea chatOutput;

    public static void primiPoruku(String primljenaPoruka) {
        Platform.runLater(new Runnable() {
            public void run() {
                TextArea chat = (TextArea) Window.scene.lookup("#chatOutput");
                chat.appendText(ServerUDP.protivnik + " > " + primljenaPoruka + "\n");
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chat();
    }

    private void chat() {
        chatInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                poruka = chatInput.getText();
                chatInput.clear();
                chatOutput.appendText(ServerUDP.prijavljeniIgrac + " > " + poruka + "\n");
                if (TrazenjeIgracaController.klijent) {
                    KlijentTCP.posalji("chat:" + poruka);
                } else {
                    ServerTCP.posalji("chat:" + poruka);
                }
            }
        });
    }

}
