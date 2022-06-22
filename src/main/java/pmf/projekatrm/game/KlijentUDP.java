package pmf.projekatrm.game;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class KlijentUDP extends Thread {

    // Prekid treda:
    public static volatile boolean running;

    // Poruka koja se salje:
    public static volatile String poruka;

    public static InetAddress adresa;

    private DatagramSocket socket;

    public KlijentUDP() {
        try {

            // Kreira broadcast adresu i broadcast socket:
            adresa = InetAddress.getByName("192.168.100.255");
            socket = new DatagramSocket();
            socket.setBroadcast(true);

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            running = true;

            // Glavna petlja klijenta:
            while (running) {
                //System.out.println("Poslato: " + poruka);

                // Pretvara String u niz bajtova, pa u paket, te ga salje na adresu:
                byte[] buffer = poruka.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, adresa, 4445);
                socket.send(packet);
                //System.out.println(poruka);

                sleep(1000);
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
