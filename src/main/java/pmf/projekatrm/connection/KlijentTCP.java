package pmf.projekatrm.connection;

import pmf.projekatrm.gui.IgraController;
import pmf.projekatrm.gui.LoginController;
import pmf.projekatrm.gui.TrazenjeIgracaController;
import pmf.projekatrm.gui.Window;

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
            IgraController.server = 1;

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
                        //System.out.println(primljenaPoruka.split(":")[i + 1]);
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

                System.out.println("Klijent > Primljena poruka: " + primljenaPoruka);

            }
            ulaz.close();
            izlaz.close();
            clientSock.close();
            running = false;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
