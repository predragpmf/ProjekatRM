package pmf.projekatrm.game;

import pmf.projekatrm.gui.Igrac;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Server extends Thread {

    // Sluzi za prekid thread-a:
    public static boolean running;

    // Primljena poruka:
    public static String poruka;

    // Korisnicko ime trenutno prijavljenog igraca:
    public static String prijavljeniIgrac;

    // UDP socket:
    private DatagramSocket socket;

    // Podaci koji se primaju:
    private byte[] buf = new byte[256];

    public Server() {
        try {
            // Bez null argumenta baca exception:
            socket = new DatagramSocket(null);

            // Na adresi 0.0.0.0 se slusa broadcast:
            InetSocketAddress address = new InetSocketAddress("0.0.0.0", 4445);

            // Omogucava bind socketa koji je vec u upotrebi:
            socket.setReuseAddress(true);

            // Postavlja socket na datu adresu i port:
            socket.bind(address);

        } catch (SocketException se) {
            se.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server es = new Server();
        es.start();
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

            // Ako naziv igraca pocinje sa ".", obrisi ga iz liste igraca:
            if (poruka.startsWith(".")) {
                Igrac.sviIgraci.removeIf(i -> i.getKorisnickoIme().equals(poruka.substring(1)));
                // Ne dodaji prijavljenog igraca u listu igraca:
            } else if (poruka.equals(prijavljeniIgrac)) {
                continue;
            } else {
                Igrac igr = new Igrac(poruka);
            }
        }
        socket.close();
    }

}
