package pmf.projekatrm.connection;

import pmf.projekatrm.gui.IgraController;
import pmf.projekatrm.gui.LoginController;
import pmf.projekatrm.gui.TrazenjeIgracaController;
import pmf.projekatrm.gui.Window;

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
            IgraController.server = 2;

            while (running) {

                zauzeto = false;

                primljenaPoruka = ulaz.readLine();

                zauzeto = true;

                if (primljenaPoruka == null) {
                    continue;
                }
                if (primljenaPoruka.startsWith("protivnik:")) {
                    ServerUDP.protivnik = primljenaPoruka.substring(10);
                    System.out.println(ServerUDP.protivnik);
                } else if (primljenaPoruka.startsWith("chat:")) {
                    IgraController.primiPoruku(primljenaPoruka.substring(5));
                } else if (primljenaPoruka.startsWith("kocke:")) {
                    int[] vrijednosti = new int[5];
                    for (int i = 0; i < 5; i++) {
                        vrijednosti[i] = Integer.parseInt(primljenaPoruka.split(":")[i + 1]);
                        System.out.println(primljenaPoruka.split(":")[i + 1]);
                    }
                    IgraController.promjeniSliku(vrijednosti);
                } else if (primljenaPoruka.startsWith("quit")) {
                    KlijentUDP.poruka = ServerUDP.prijavljeniIgrac;
                    if (!LoginController.udpKlijent.isAlive()) {
                        LoginController.udpKlijent.start();
                    }
                    if (!LoginController.udpServer.isAlive()) {
                        new ServerUDP().start();
                    }
                    TrazenjeIgracaController.ig = null;
                    ServerUDP.protivnik = "";
                    Window.promjeniScenu("TrazenjeIgraca.fxml", "Traženje igrača", 800, 600);
                    break;
                } else if (primljenaPoruka.startsWith("tabela:")) {
                    String[] vrijednosti = primljenaPoruka.split(":");
                    IgraController.azurirajTabelu(vrijednosti);
                }

                System.out.println("Server > Primljena poruka: " + primljenaPoruka);
            }
            ulaz.close();
            izlaz.close();
            clientSock.close();
            servSock.close();
            running = false;

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
