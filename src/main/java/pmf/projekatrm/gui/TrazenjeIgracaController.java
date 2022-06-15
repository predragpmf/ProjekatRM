package pmf.projekatrm.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import pmf.projekatrm.game.Server;

import java.net.URL;
import java.util.ResourceBundle;

public class TrazenjeIgracaController implements Initializable {

    @FXML
    private Button trazenjeIspisButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // TEMP:
        trazenjeIspisButton.setOnAction(event -> {
            for (String str : Server.igraci) {
                System.out.print(str + " ");
            }
            System.out.println();
        });

    }

}
