package pmf.projekatrm.game;

import pmf.projekatrm.gui.IgraController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServerTCP extends Thread {

    // Prekid petlje treda:
    public static volatile boolean running;

    // Primljena poruka:
    public static volatile String primljenaPoruka;

    // Port servera:
    public static int port;

    // Stanje servera:
    private static boolean zauzeto;

    private static BufferedReader ulaz;
    private static PrintWriter izlaz;


    public static void posalji(String porukaSlanje) {
        while (true) {
            if (!zauzeto) {
                izlaz.println(porukaSlanje);
                break;
            }
        }
    }


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
            Socket clientSock = servSock.accept();
            System.out.println("Novi socket prihvacen.");
            ulaz = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
            izlaz = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSock.getOutputStream())), true);
            izlaz.println("protivnik:" + ServerUDP.prijavljeniIgrac);

            while (running) {

                zauzeto = false;

                primljenaPoruka = ulaz.readLine();

                zauzeto = true;

                if (primljenaPoruka.startsWith("protivnik:")) {
                    ServerUDP.protivnik = primljenaPoruka.substring(10);
                } else if (primljenaPoruka.startsWith("chat:")) {
                    IgraController.primiPoruku(primljenaPoruka.substring(5));
                }
                //sleep(1000);
                System.out.println("Server > Primljena poruka: " + primljenaPoruka);
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
