package pmf.projekatrm.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import pmf.projekatrm.connection.KlijentTCP;
import pmf.projekatrm.connection.KlijentUDP;
import pmf.projekatrm.connection.ServerTCP;
import pmf.projekatrm.connection.ServerUDP;
import pmf.projekatrm.game.Jamb;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class IgraController implements Initializable {

    // klijent 1, server 2:
    public static int server;

    // Broj poteza:
    public static int potez;

    public static Jamb jamb;

    // Lista kocki koje se nece ponovo bacati:
    public static ArrayList<Integer> odabraneKocke = new ArrayList<>();

    // Broj pritisnutih toggle tipki na tabeli:
    private int brojPritisnutih;

    @FXML
    private Label pobjednikLabel;
    @FXML
    private TextField chatInput;
    @FXML
    private TextArea chatOutput;
    @FXML
    private Button baciKocke;
    @FXML
    private Button izadjiButton;
    @FXML
    private ImageView kocka1;
    @FXML
    private ImageView kocka2;
    @FXML
    private ImageView kocka3;
    @FXML
    private ImageView kocka4;
    @FXML
    private ImageView kocka5;


    // Igrac kolona tabele:
    @FXML
    private ToggleButton toggle1;
    @FXML
    private ToggleButton toggle2;
    @FXML
    private ToggleButton toggle3;
    @FXML
    private ToggleButton toggle4;
    @FXML
    private ToggleButton toggle5;
    @FXML
    private ToggleButton toggle6;
    @FXML
    private Label sumaIgracLabel;
    @FXML
    private Label bonusIgracLabel;
    @FXML
    private ToggleButton threeOAKToggle;
    @FXML
    private ToggleButton fourOAKToggle;
    @FXML
    private ToggleButton fullHouseToggle;
    @FXML
    private ToggleButton smallStraightToggle;
    @FXML
    private ToggleButton largeStraightToggle;
    @FXML
    private ToggleButton sansaToggle;
    @FXML
    private ToggleButton jambToggle;
    @FXML
    private Label ukupnoIgracLabel;

    // Protivnik kolona tabele:
    @FXML
    private Label ukupnoProtivnikLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        jamb = new Jamb();
        ucitajProtivnika();
        chat();
        baci();
        pritisakTabele();
        izlaz();
    }


    // Kada primi tabelu od protivnika, unosi vrijednosti u svoju tabelu:
    public static void azurirajTabelu(String[] vrijednosti) {
        Platform.runLater(new Runnable() {
            public void run() {
                Button baciKocke = (Button) Window.scene.lookup("#baciKocke");
                baciKocke.setDisable(false);
                Label label1 = (Label) Window.scene.lookup("#label1");
                Label label2 = (Label) Window.scene.lookup("#label2");
                Label label3 = (Label) Window.scene.lookup("#label3");
                Label label4 = (Label) Window.scene.lookup("#label4");
                Label label5 = (Label) Window.scene.lookup("#label5");
                Label label6 = (Label) Window.scene.lookup("#label6");
                Label pobjednikLabel = (Label) Window.scene.lookup("#pobjednikLabel");
                Label sumaProtivnikLabel = (Label) Window.scene.lookup("#sumaProtivnikLabel");
                Label bonusProtivnikLabel = (Label) Window.scene.lookup("#bonusProtivnikLabel");
                Label threeOAKLabel = (Label) Window.scene.lookup("#threeOAKLabel");
                Label fourOAKLabel = (Label) Window.scene.lookup("#fourOAKLabel");
                Label fullHouse = (Label) Window.scene.lookup("#fullHouseLabel");
                Label smallStraightLabel = (Label) Window.scene.lookup("#smallStraightLabel");
                Label largeStraightLabel = (Label) Window.scene.lookup("#largeStraightLabel");
                Label sansaLabel = (Label) Window.scene.lookup("#sansaLabel");
                Label jambLabel = (Label) Window.scene.lookup("#jambLabel");
                Label ukupnoProtivnikLabel = (Label) Window.scene.lookup("#ukupnoProtivnikLabel");
                Label ukupnoIgracLabel = (Label) Window.scene.lookup("#ukupnoIgracLabel");
                label1.setText(vrijednosti[1]);
                label2.setText(vrijednosti[2]);
                label3.setText(vrijednosti[3]);
                label4.setText(vrijednosti[4]);
                label5.setText(vrijednosti[5]);
                label6.setText(vrijednosti[6]);
                sumaProtivnikLabel.setText(vrijednosti[7]);
                bonusProtivnikLabel.setText(vrijednosti[8]);
                threeOAKLabel.setText(vrijednosti[9]);
                fourOAKLabel.setText(vrijednosti[10]);
                fullHouse.setText(vrijednosti[11]);
                smallStraightLabel.setText(vrijednosti[12]);
                largeStraightLabel.setText(vrijednosti[13]);
                sansaLabel.setText(vrijednosti[14]);
                jambLabel.setText(vrijednosti[15]);
                ukupnoProtivnikLabel.setText(vrijednosti[16]);

                // Prikazuje poruku na zavrsetku igre:
                if (Integer.parseInt(ukupnoProtivnikLabel.getText()) != 0 && Integer.parseInt(ukupnoIgracLabel.getText()) != 0) {
                    System.out.println("Drugi");
                    if (Integer.parseInt(ukupnoIgracLabel.getText()) > Integer.parseInt(ukupnoProtivnikLabel.getText())) {
                        pobjednikLabel.setText("Pobijedili ste!");
                        pobjednikLabel.setTextFill(Color.GREEN);
                    } else if (Integer.parseInt(ukupnoIgracLabel.getText()) < Integer.parseInt(ukupnoProtivnikLabel.getText())) {
                        pobjednikLabel.setText("Izgubili ste.");
                        pobjednikLabel.setTextFill(Color.RED);
                    } else {
                        pobjednikLabel.setText("Nerijeseno!");
                    }
                }
            }
        });
    }


    // Kada primi poruku od protivnika, ispisuje je na chat:
    public static void primiPoruku(String primljenaPoruka) {
        Platform.runLater(new Runnable() {
            public void run() {
                TextArea chat = (TextArea) Window.scene.lookup("#chatOutput");
                chat.appendText(ServerUDP.protivnik + " > " + primljenaPoruka + "\n");
            }
        });
    }


    // Mijenja sliku jedne kocke:
    public static void promjeniSliku(int pozicija, int vrijednost) {
        Platform.runLater(new Runnable() {
            public void run() {
                ImageView slika = (ImageView) Window.scene.lookup("#kocka" + (pozicija + 1));
                File file = new File("src/main/resources/pmf/projekatrm/game/" + vrijednost + ".png");
                Image image = new Image(file.toURI().toString());
                slika.setImage(image);
            }
        });
    }


    // Mijenja slike vise kocki:
    public static void promjeniSliku(int[] vrijednosti) {
        Platform.runLater(new Runnable() {
            public void run() {
                for (int i = 0; i < 5; i++) {
                    ImageView slika = (ImageView) Window.scene.lookup("#kockaProtivnika" + (i + 1));
                    File file = new File("src/main/resources/pmf/projekatrm/game/" + vrijednosti[i] + ".png");
                    Image image = new Image(file.toURI().toString());
                    slika.setImage(image);
                }
            }
        });
    }


    // Pritisak tipki na tabeli:
    private void pritisakTabele() {
        toggle1.setOnAction(event -> {
            toggle1.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
        toggle2.setOnAction(event -> {
            toggle2.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
        toggle3.setOnAction(event -> {
            toggle3.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
        toggle4.setOnAction(event -> {
            toggle4.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
        toggle5.setOnAction(event -> {
            toggle5.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
        toggle6.setOnAction(event -> {
            toggle6.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
        threeOAKToggle.setOnAction(event -> {
            threeOAKToggle.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
        fourOAKToggle.setOnAction(event -> {
            fourOAKToggle.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
        fullHouseToggle.setOnAction(event -> {
            fullHouseToggle.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
        smallStraightToggle.setOnAction(event -> {
            smallStraightToggle.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
        largeStraightToggle.setOnAction(event -> {
            largeStraightToggle.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
        sansaToggle.setOnAction(event -> {
            sansaToggle.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
        jambToggle.setOnAction(event -> {
            jambToggle.setDisable(true);
            brojPritisnutih++;
            sljedeciPotez();
        });
    }


    // Na pritisak tipke u tabeli:
    private void sljedeciPotez() {
        resetKocke();
        baciKocke.setDisable(true);
        potez = 0;

        // Resetuje vrijednosti na tabeli:
        if (!toggle1.isDisabled()) {
            toggle1.setText("0");
        }
        if (!toggle2.isDisabled()) {
            toggle2.setText("0");
        }
        if (!toggle3.isDisabled()) {
            toggle3.setText("0");
        }
        if (!toggle4.isDisabled()) {
            toggle4.setText("0");
        }
        if (!toggle5.isDisabled()) {
            toggle5.setText("0");
        }
        if (!toggle6.isDisabled()) {
            toggle6.setText("0");
        }
        if (!threeOAKToggle.isDisabled()) {
            threeOAKToggle.setText("0");
        }
        if (!fourOAKToggle.isDisabled()) {
            fourOAKToggle.setText("0");
        }
        if (!fullHouseToggle.isDisabled()) {
            fullHouseToggle.setText("0");
        }
        if (!smallStraightToggle.isDisabled()) {
            smallStraightToggle.setText("0");
        }
        if (!largeStraightToggle.isDisabled()) {
            largeStraightToggle.setText("0");
        }
        if (!sansaToggle.isDisabled()) {
            sansaToggle.setText("0");
        }
        if (!jambToggle.isDisabled()) {
            jambToggle.setText("0");
        }

        // Ako su sve vrijednosti iz gornje tabele odabrane, izracunaj sumu i bonus:
        if (toggle1.isDisabled() && toggle2.isDisabled() && toggle3.isDisabled() && toggle4.isDisabled() &&
                toggle5.isDisabled() && toggle6.isDisabled()) {
            int suma = Integer.parseInt(toggle1.getText()) +
                    Integer.parseInt(toggle2.getText()) + Integer.parseInt(toggle3.getText()) +
                    Integer.parseInt(toggle4.getText()) + Integer.parseInt(toggle5.getText()) +
                    Integer.parseInt(toggle6.getText());
            sumaIgracLabel.setText(Integer.toString(suma));
            if (suma >= 63) {
                bonusIgracLabel.setText("35");
            } else {
                bonusIgracLabel.setText("0");
            }
        }
        // Ako su sve vrijednosti odabrane, izracunaj ukupni rezultat:
        int suma = 0;
        if (brojPritisnutih == 13) {
            suma = Integer.parseInt(sumaIgracLabel.getText()) + Integer.parseInt(bonusIgracLabel.getText()) +
                    Integer.parseInt(threeOAKToggle.getText()) + Integer.parseInt(fourOAKToggle.getText()) +
                    Integer.parseInt(fullHouseToggle.getText()) + Integer.parseInt(smallStraightToggle.getText()) +
                    Integer.parseInt(largeStraightToggle.getText()) + Integer.parseInt(sansaToggle.getText()) +
                    Integer.parseInt(jambToggle.getText());
            ukupnoIgracLabel.setText(Integer.toString(suma));
        }

        // Salje vrijednosti tabele protivniku:
        String poruka = "tabela:" + toggle1.getText() + ":" + toggle2.getText() + ":" + toggle3.getText() + ":" +
                toggle4.getText() + ":" + toggle5.getText() + ":" + toggle6.getText() + ":" +
                sumaIgracLabel.getText() + ":" + bonusIgracLabel.getText() + ":" + threeOAKToggle.getText() + ":" +
                fourOAKToggle.getText() + ":" + fullHouseToggle.getText() + ":" + smallStraightToggle.getText() +
                ":" + largeStraightToggle.getText() + ":" + sansaToggle.getText() + ":" + jambToggle.getText() +
                ":" + ukupnoIgracLabel.getText();
        if (server == 1) {
            KlijentTCP.posalji(poruka);
        } else if (server == 2) {
            ServerTCP.posalji(poruka);
        }

        // Prikazuje poruku na zavrsetku igre:
        String sumaProtivnikaStr = ukupnoProtivnikLabel.getText();
        if (sumaProtivnikaStr.equals("-")) {
            return;
        }
        int sumaProtivnika = Integer.parseInt(sumaProtivnikaStr);
        if (sumaProtivnika > 0) {
            System.out.println("Prvi");
            if (suma > sumaProtivnika) {
                pobjednikLabel.setText("Pobjedili ste!");
                pobjednikLabel.setTextFill(Color.GREEN);
            } else if (suma < sumaProtivnika) {
                pobjednikLabel.setText("Izgubili ste.");
                pobjednikLabel.setTextFill(Color.RED);
            } else {
                pobjednikLabel.setText("Nerijeseno!");
            }
        }
    }


    // Ucitava ime protivnika iznad tabele:
    private void ucitajProtivnika() {

        // Odredjuje ko ima prvi potez:
        if (server == 1) {
            baciKocke.setDisable(true);
        }

        // Pokrece novi thread u pozadini, koji ceka dok protivnik ne bude poznat:
        (new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    public void run() {
                        Label protivnik = (Label) Window.scene.lookup("#protivnikLabel");
                        protivnik.setText(ServerUDP.protivnik);
                    }
                });
            }
        }).start();
    }


    // Na pritisak "Enter" cita poruku iz textboxa i salje je protivniku:
    private void chat() {
        chatInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String poruka = chatInput.getText();
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


    // Pritisak tipke "Baci"
    private void baci() {
        baciKocke.setOnAction(event -> {

            if (odabraneKocke.isEmpty()) {
                jamb.baciSveKocke();
            } else {
                jamb.baciSveOsim(odabraneKocke);
            }

            // Salje vrijednosti kocki protivniku:
            if (TrazenjeIgracaController.klijent) {
                KlijentTCP.posalji("kocke:" + jamb.getSveKocke()[0].getVrijednost() + ":"
                        + jamb.getSveKocke()[1].getVrijednost() + ":" + jamb.getSveKocke()[2].getVrijednost() + ":"
                        + jamb.getSveKocke()[3].getVrijednost() + ":" + jamb.getSveKocke()[4].getVrijednost());
            } else {
                ServerTCP.posalji("kocke:" + jamb.getSveKocke()[0].getVrijednost() + ":"
                        + jamb.getSveKocke()[1].getVrijednost() + ":" + jamb.getSveKocke()[2].getVrijednost() + ":"
                        + jamb.getSveKocke()[3].getVrijednost() + ":" + jamb.getSveKocke()[4].getVrijednost());
            }
            for (int i = 0; i < 5; i++) {
                promjeniSliku(i, jamb.getSveKocke()[i].getVrijednost());
            }

            // Racunanje vrijednosti na tabeli:
            if (!toggle1.isDisabled()) {
                toggle1.setText(Integer.toString(jamb.getGornjiSkor(1)));
            }
            if (!toggle2.isDisabled()) {
                toggle2.setText(Integer.toString(jamb.getGornjiSkor(2)));
            }
            if (!toggle3.isDisabled()) {
                toggle3.setText(Integer.toString(jamb.getGornjiSkor(3)));
            }
            if (!toggle4.isDisabled()) {
                toggle4.setText(Integer.toString(jamb.getGornjiSkor(4)));
            }
            if (!toggle5.isDisabled()) {
                toggle5.setText(Integer.toString(jamb.getGornjiSkor(5)));
            }
            if (!toggle6.isDisabled()) {
                toggle6.setText(Integer.toString(jamb.getGornjiSkor(6)));
            }
            if (!threeOAKToggle.isDisabled()) {
                threeOAKToggle.setText(Integer.toString(jamb.threeOfAKind()));
            }
            if (!fourOAKToggle.isDisabled()) {
                fourOAKToggle.setText(Integer.toString(jamb.fourOfAKind()));
            }
            if (!fullHouseToggle.isDisabled()) {
                fullHouseToggle.setText(Integer.toString(jamb.fullHouse()));
            }
            if (!smallStraightToggle.isDisabled()) {
                smallStraightToggle.setText(Integer.toString(jamb.smallStraight()));
            }
            if (!largeStraightToggle.isDisabled()) {
                largeStraightToggle.setText(Integer.toString(jamb.largeStraight()));
            }
            if (!sansaToggle.isDisabled()) {
                sansaToggle.setText(Integer.toString(jamb.sumirajSve()));
            }
            if (!jambToggle.isDisabled()) {
                jambToggle.setText(Integer.toString(jamb.fiveOfAKind()));
            }

            potez++;
            if (potez > 2) {
                baciKocke.setDisable(true);
                potez = 0;
            }
        });
    }


    // Izlaz iz igre:
    private void izlaz() {
        izadjiButton.setOnAction(event -> {
            if (TrazenjeIgracaController.klijent) {
                KlijentTCP.posalji("quit");
                KlijentTCP.running = false;
            } else {
                ServerTCP.posalji("quit");
                ServerTCP.running = false;
            }

            KlijentUDP.poruka = ServerUDP.prijavljeniIgrac;
            if (!LoginController.udpKlijent.isAlive()) {
                LoginController.udpKlijent.start();
            }
            if (!LoginController.udpServer.isAlive()) {
                new ServerUDP().start();
            }
            TrazenjeIgracaController.ig = null;
            ServerUDP.protivnik = "";
            IgraController.server = 0;
            IgraController.potez = 0;
            IgraController.odabraneKocke.clear();
            IgraController.jamb = null;
            baciKocke.setDisable(false);
            Window.promjeniScenu("TrazenjeIgraca.fxml", "Traženje igrača", 800, 600);
        });
    }


    // Odabir kocki koje se nece bacati:
    public void ostaviKocku() {

        if (kocka1.isHover()) {
            if (kocka1.getOpacity() > 0.5) {
                kocka1.setOpacity(0.33);
                odabraneKocke.add(1);
            } else {
                kocka1.setOpacity(1);
                odabraneKocke.removeIf(i -> i == 1);
            }
        } else if (kocka2.isHover()) {
            if (kocka2.getOpacity() > 0.5) {
                kocka2.setOpacity(0.33);
                odabraneKocke.add(2);
            } else {
                kocka2.setOpacity(1);
                odabraneKocke.removeIf(i -> i == 2);
            }
        } else if (kocka3.isHover()) {
            if (kocka3.getOpacity() > 0.5) {
                kocka3.setOpacity(0.33);
                odabraneKocke.add(3);
            } else {
                kocka3.setOpacity(1);
                odabraneKocke.removeIf(i -> i == 3);
            }
        } else if (kocka4.isHover()) {
            if (kocka4.getOpacity() > 0.5) {
                kocka4.setOpacity(0.33);
                odabraneKocke.add(4);
            } else {
                kocka4.setOpacity(1);
                odabraneKocke.removeIf(i -> i == 4);
            }
        } else if (kocka5.isHover()) {
            odabraneKocke.add(5);
            if (kocka5.getOpacity() > 0.5) {
                kocka5.setOpacity(0.33);
            } else {
                kocka5.setOpacity(1);
                odabraneKocke.removeIf(i -> i == 5);
            }
        }

    }


    private void resetKocke() {
        kocka1.setOpacity(1);
        odabraneKocke.removeIf(i -> i == 1);
        kocka2.setOpacity(1);
        odabraneKocke.removeIf(i -> i == 2);
        kocka3.setOpacity(1);
        odabraneKocke.removeIf(i -> i == 3);
        kocka4.setOpacity(1);
        odabraneKocke.removeIf(i -> i == 4);
        kocka5.setOpacity(1);
        odabraneKocke.removeIf(i -> i == 5);
    }

}
