package pmf.projekatrm.game;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class KlijentTCP extends Thread {

    // Prekid treda:
    public static volatile boolean running;

    // Poruka koja se salje:
    public static volatile String poruka;

    // Port servera:
    public static int port;


    @Override
    public void run() {
        try {
            running = true;
            System.out.println("Pokretanje TCP klijenta...");
            while (running) {
                InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
                Socket clientSock = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(clientSock.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));

                // Posalji poruku:
                out.println(poruka);

                System.out.println("Poslata poruka: " + poruka);

                // Procitaj odgovor:
                String odgovor = in.readLine();

                //System.out.println("success");
                in.close();
                out.close();
                clientSock.close();
                sleep(1000);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
