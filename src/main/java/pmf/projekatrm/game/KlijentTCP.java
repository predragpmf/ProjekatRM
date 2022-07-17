package pmf.projekatrm.game;

import pmf.projekatrm.gui.IgraController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class KlijentTCP extends Thread {

    // Prekid treda:
    public static volatile boolean running;

    public static volatile String primljenaPoruka;

    // Adresa severa:
    public static InetAddress serverAddress;

    // Port servera:
    public static int port;

    private static Socket clientSock;

    private static PrintWriter izlaz;
    private static BufferedReader ulaz;

    private static boolean zauzeto;


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
            System.out.println("Pokretanje TCP klijenta...");
            serverAddress = InetAddress.getByName("127.0.0.1");

            clientSock = new Socket(serverAddress, port);
            ulaz = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
            izlaz = new PrintWriter(clientSock.getOutputStream(), true);
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

                System.out.println("Klijent > Primljena poruka:" + primljenaPoruka);

                //ulaz.close();
                //izlaz.close();
                //clientSock.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
