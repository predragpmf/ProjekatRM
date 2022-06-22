package pmf.projekatrm.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pmf.projekatrm.game.KlijentUDP;

import java.io.IOException;

public class Window extends Application {

    private static Stage loginStage;

    public static Stage getStage() {
        return loginStage;
    }

    // Metoda za promjenu trenutne scene:
    public static void promjeniScenu(String fxmlPutanja, String naslov, int sirina, int visina) {
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Window.class.getResource(fxmlPutanja));
                    Scene scene = new Scene(fxmlLoader.load(), sirina, visina);
                    getStage().setResizable(false);
                    getStage().setTitle(naslov);
                    getStage().setScene(scene);
                } catch (IOException e) {
                    System.err.println("Greska pri ucitavanju fxml fajla!");
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Promjena trenutne scene iz sporednog treda je moguca samo pomocu ove metode:
    /*
    public static void pokreniIgru() {
        Platform.runLater( new Runnable() { public void run() {
                Window.promjeniScenu("Igra.fxml", "Igra", 800, 600);
            }});
    }
     */

    @Override
    public void start(Stage primaryStage) {
        try {
            loginStage = primaryStage;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            loginStage.setTitle("Prijava");
            loginStage.setScene(scene);
            loginStage.show();

            // Ako se zatvori prozor, gasi server:
            primaryStage.setOnCloseRequest(e -> {
                System.out.println("Gasenje servera!");
                // Posalji svima paket = ".username" da se obrise taj username:
                KlijentUDP.poruka = "." + KlijentUDP.poruka;
                // Spavaj 1s da bi poruka imala dovoljno vremena da ode:
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                // Zatvori sve:
                System.exit(0);
            });

        } catch (IOException e) {
            System.err.println("Greska pri ucitavanju fxml fajla!");
            e.printStackTrace();
        }
    }

}
