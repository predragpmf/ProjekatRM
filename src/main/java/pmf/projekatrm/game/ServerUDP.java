package pmf.projekatrm.game;

import pmf.projekatrm.gui.TrazenjeIgracaController;
import pmf.projekatrm.gui.Window;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
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
    public static String protivnik;
    public static CountDownLatch prihvacenZahtjev = new CountDownLatch(1);
    // UDP socket:
    private DatagramSocket socket;
    // Podaci koji se primaju:
    private byte[] buf = new byte[256];

    public ServerUDP() {
        try {
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
                // Ne dodaji prijavljenog igraca u listu igraca:
            } else if (poruka.equals(prijavljeniIgrac)) {
                continue;
                // Ako poruka pocinje sa "connect:" i igrac je jednak trenutnom, pokreni TCP server i ugasi UDP server:
            } else if (poruka.startsWith("connect:") && poruka.substring(8).equals(prijavljeniIgrac)) {
                System.out.println("Poruka pocinje sa connect:");
                new ServerTCP().start();
                Window.promjeniScenu("Igra.fxml", "Igra", 800, 600);
                break;
                // Ako poruka pocinje sa "connect:" a korisnik je nepoznat, ignorisi poruku:
            } else if (poruka.startsWith("connect:")) {
                continue;
                // Ako poruka pocinje sa "busy:" i igrac je jednak odabranom u tabeli, sacuvaj poruku na promjenljivu "protivnik":
            } else if (TrazenjeIgracaController.ig != null) {
                if (poruka.startsWith("busy:") && (poruka.split(":")[2].equals(TrazenjeIgracaController.ig.getKorisnickoIme()))) {
                    protivnik = poruka;
                    prihvacenZahtjev.countDown();
                    continue;
                }
                // Ako poruka pocinje sa "busy:", oznaci tog igraca kao zauzetog (trenutno u igri):
            } else if (poruka.startsWith("busy:")) {
                String igrac = poruka.split(":")[2];
                for (Igrac i : Igrac.sviIgraci) {
                    if (i.getKorisnickoIme().equals(igrac)) {
                        i.setStanje("zauzet");
                    }
                }
                continue;
                // Ako uslovi nisu ispunjeni, u poruci se nalazi korisnicko ime igraca:
            } else {
                new Igrac(poruka);
            }
        }
        System.out.println("Gasenje UDP servera...");
        socket.close();
    }

}
