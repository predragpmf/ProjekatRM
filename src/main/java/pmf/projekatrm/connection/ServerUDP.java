package pmf.projekatrm.connection;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import pmf.projekatrm.game.Igrac;
import pmf.projekatrm.gui.TrazenjeIgracaController;
import pmf.projekatrm.gui.Window;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class ServerUDP extends Thread {

    // Prekid treda:
    public static volatile boolean running;

    // Primljena poruka:
    public static volatile String poruka;

    // IP adresa servera:
    public static InetSocketAddress adresa;

    // Korisnicko ime trenutno prijavljenog igraca:
    public static volatile String prijavljeniIgrac;

    // Poruka sa protivnikom i portom igre:
    public static String protivnik;

    // Obavjestava TrazenjeIgracaController da je stigla poruka sa protivnikom:
    public static CountDownLatch prihvacenZahtjev;

    // UDP socket:
    private DatagramSocket socket;

    // Podaci koji se primaju:
    private byte[] buf = new byte[256];

    private boolean prolaz = true;
    private boolean stanje = false;
    //private boolean test = true;

    public ServerUDP() {
        try {
            prihvacenZahtjev = new CountDownLatch(1);
            // Bez null argumenta baca exception:
            socket = new DatagramSocket(null);

            // Na adresi 0.0.0.0 se slusa broadcast:
            adresa = new InetSocketAddress("0.0.0.0", 4445);

            // Omogucava bind socketa koji je vec u upotrebi:
            socket.setReuseAddress(true);

            // Postavlja socket na datu adresu i port:
            socket.bind(adresa);

        } catch (SocketException se) {
            se.printStackTrace();
        }
    }

    public void run() {

        running = true;

        // Glavna petlja servera:
        while (running) {

            // Kreira novi paket i u njega preuzima podatke:
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Pretvara niz bajtova u String:
            poruka = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Primljeno: " + poruka);

            // Ako naziv igraca pocinje sa ".", obrisi ga iz liste igraca:
            if (poruka.startsWith(".")) {
                Igrac.sviIgraci.removeIf(i -> i.getKorisnickoIme().equals(poruka.substring(1)));
                continue;
            }
            // Ne dodaji prijavljenog igraca u listu igraca:
            if (poruka.equals(prijavljeniIgrac)) {
                continue;
            }
            // Ako poruka pocinje sa "connect:" i igrac je jednak trenutnom, pokreni TCP server i ugasi UDP server:
            if (poruka.startsWith("connect:") && poruka.substring(8).equals(prijavljeniIgrac)) {
                //System.out.println("Poruka pocinje sa connect:");
                //if (test) {
                zahtjev();
                if (stanje) {
                    new ServerTCP().start();
                    Window.promjeniScenu("Igra.fxml", "Igra", 800, 600);
                    break;
                }
                //}
            }
            // Ako poruka pocinje sa "connect:" a korisnik je nepoznat, ignorisi poruku:
            if (poruka.startsWith("connect:")) {
                continue;
            }
            // Ako poruka pocinje sa "busy:" i igrac je jednak odabranom u tabeli, sacuvaj poruku na promjenljivu "protivnik":
            if (TrazenjeIgracaController.ig != null) {
                if (poruka.startsWith("busy:") && (poruka.split(":")[2].equals(TrazenjeIgracaController.ig.getKorisnickoIme()))) {
                    System.out.println("Primljen protivnik: " + poruka);
                    protivnik = poruka;
                    prihvacenZahtjev.countDown();
                    break;
                }
            }
            // Ako poruka pocinje sa "busy:", oznaci tog igraca kao zauzetog (trenutno u igri):
            if (poruka.startsWith("busy:")) {
                String igrac = poruka.split(":")[2];
                for (Igrac i : Igrac.sviIgraci) {
                    if (i.getKorisnickoIme().equals(igrac) && !i.getStanje().equals("zauzet")) {
                        i.setStanje("zauzet");
                    }
                }
            } else {
                // Ako uslovi nisu ispunjeni, u poruci se nalazi korisnicko ime igraca:
                new Igrac(poruka);
            }
        }
        System.out.println("Gasenje UDP servera...");
        socket.close();
    }

    private void zahtjev() {
        if (prolaz) {
            prolaz = false;
            //test = false;
            Platform.runLater(new Runnable() {
                public void run() {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Zahtjev za igru");
                    alert.setContentText("Da li želite da prihvatite?");

                    ButtonType buttonTypeOne = new ButtonType("DA");
                    ButtonType buttonTypeTwo = new ButtonType("NE");

                    alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeOne) {
                        //test = true;
                        stanje = true;
                    } else if (result.get() == buttonTypeTwo) {
                        alert.close();
                    }
                }
            });
        }
    }

}
