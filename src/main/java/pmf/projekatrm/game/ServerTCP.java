package pmf.projekatrm.game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServerTCP extends Thread {

    // Prekid treda:
    public static volatile boolean running;

    // Primljena poruka:
    public static volatile String poruka;

    // Port servera:
    public static int port;


    @Override
    public void run() {
        try {
            running = true;

            Random rand = new Random();
            port = rand.nextInt(48125) + 1025;

            ServerSocket servSock = new ServerSocket(port);
            System.out.println("Pokretanje TCP servera...");
            System.out.println("Port TCP Servera je: " + ServerTCP.port);
            KlijentUDP.poruka = "busy:" + ServerTCP.port + ":" + ServerUDP.prijavljeniIgrac;

            while (running) {
                Socket clientSock = servSock.accept();
                //System.out.println("TCP poruka prihvacena");

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSock.getOutputStream())), true);

                poruka = in.readLine();

                System.out.println("Primljena poruka: " + poruka);

                //System.out.println("primljeno");
                out.println("success");
                //sleep(1000);
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
