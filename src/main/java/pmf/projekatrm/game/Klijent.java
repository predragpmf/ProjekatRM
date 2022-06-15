package pmf.projekatrm.game;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Klijent extends Thread {

    // Poruka koja se salje
    public static String poruka;

    public static boolean running;

    public static void main(String[] args) {
        Klijent bc = new Klijent();
        bc.start();
    }

    @Override
    public void run() {
        try {
            running = true;
            // Kreira broadcast adresu i broadcast socket:
            InetAddress address = InetAddress.getByName("255.255.255.255");
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            // Glavna petlja klijenta:
            while (running) {

                // Pretvara String u niz bajtova, pa u paket, te ga salje na adresu:
                byte[] buffer = poruka.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 4445);
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
