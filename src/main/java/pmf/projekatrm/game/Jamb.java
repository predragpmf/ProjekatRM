package pmf.projekatrm.game;

import java.util.ArrayList;
import java.util.Arrays;

public class Jamb {

    private Kocka[] sveKocke;

    // Prebrojane vrijednosti u listi:
    private int[] brojVrijednosti;

    private int sumaVrijednosti;

    public Jamb() {
        sveKocke = new Kocka[5];
        brojVrijednosti = new int[6];
        for (int i = 0; i < 5; i++) {
            sveKocke[i] = new Kocka();
        }
        sumaVrijednosti = sumirajSve();
        prebrojSve();

    }

    // Baca sve kocke:
    public void baciSveKocke() {
        for (Kocka k : sveKocke) {
            k.baci();
        }
        sumaVrijednosti = sumirajSve();
        prebrojSve();
    }

    // Baca samo kocku pod datim rednim brojem:
    public void baciKocku(int broj) {
        try {
            sveKocke[broj - 1].baci();
            sumaVrijednosti = sumirajSve();
            prebrojSve();
        } catch (Exception e) {
            System.err.println("Kocka sa datim rednim brojem ne postoji!");
            e.printStackTrace();
        }
    }

    // Vraca vrijednost date kocke:
    public int getVrijednostKocke(int broj) {
        try {
            return sveKocke[broj - 1].getVrijednost();
        } catch (Exception e) {
            System.err.println("Kocka sa datim rednim brojem ne postoji!");
            e.printStackTrace();
            return 0;
        }
    }

    public Kocka[] getSveKocke() {
        return sveKocke;
    }

    // Ispisuje vrijednost svih kocki:
    public void ispisiKocke() {
        for (Kocka k : sveKocke) {
            System.out.print(k.getVrijednost() + " ");
        }
        System.out.println();
    }

    // Vraca broj kocki sa datom vrijednosti:
    public int prebroj(int vrijednost) {
        int broj = 0;
        for (Kocka k : sveKocke) {
            if (k.getVrijednost() == vrijednost) {
                broj++;
            }
        }
        return broj;
    }

    // Prebrojava sve vrijednosti kocki:
    public void prebrojSve() {
        for (int i = 0; i < 6; i++) {
            brojVrijednosti[i] = prebroj(i + 1);
            System.out.println("Broj " + (i + 1) + " je = " + brojVrijednosti[i]);
        }
    }

    // Baca sve kocke osim onih koje se nalaze u listi "ostavi":
    public void baciSveOsim(ArrayList<Integer> ostavi) {
        for (int i = 0; i < 5; i++) {
            if (!ostavi.contains(i + 1))
                baciKocku(i + 1);
        }
        sumaVrijednosti = sumirajSve();
    }

    public int getGornjiSkor(int vrijednost) {
        return brojVrijednosti[vrijednost - 1] * vrijednost;
    }

    public int sumirajSve() {
        int sum = 0;
        for (Kocka k : sveKocke) {
            sum += k.getVrijednost();
        }
        return sum;
    }

    public int threeOfAKind() {
        for (int i = 0; i < 6; i++) {
            if (brojVrijednosti[i] >= 3) {
                return sumaVrijednosti;
            }
        }
        return 0;
    }

    public int fourOfAKind() {
        for (int i = 0; i < 6; i++) {
            if (brojVrijednosti[i] >= 4) {
                return sumaVrijednosti;
            }
        }
        return 0;
    }

    public int fiveOfAKind() {
        for (int i = 0; i < 6; i++) {
            if (brojVrijednosti[i] >= 5) {
                return 50;
            }
        }
        return 0;
    }

    public int fullHouse() {
        boolean tri = false;
        boolean par = false;
        for (int i = 0; i < 6; i++) {
            if (brojVrijednosti[i] == 3) {
                tri = true;
            }
            if (brojVrijednosti[i] == 2) {
                par = true;
            }
        }
        if (tri && par) {
            return 25;
        } else {
            return 0;
        }
    }

    private int[] getSortedKocke(Kocka[] unos) {
        int[] rezultat = new int[unos.length];
        for(int i = 0; i < unos.length; i++){
            rezultat[i] = unos[i].getVrijednost();
        }
        Arrays.sort(rezultat);
        return rezultat;
    }

    public int smallStraight() {
        int brUzastopnih;
        int[] vrijednosti = getSortedKocke(sveKocke);
        for(int i = 0; i < 5; i++) {
            if(i >= 3) {
                brUzastopnih = 0;
                int prethodna = vrijednosti[i-3];
                for(int j = i - 2; j <= i; j++) {
                    int trenutna = vrijednosti[j];
                    if(trenutna != prethodna + 1) {
                        break;
                    }
                    brUzastopnih += 1;
                    prethodna = trenutna;
                }
                if(brUzastopnih>=3) {
                    return 30;
                }
            }
        }
        return 0;
    }

    public int largeStraight() {
        int[] vrijednosti = getSortedKocke(sveKocke);
        int prethodna = vrijednosti[0];
        for(int i = 1; i < 5; i++) {
            int trenutna = vrijednosti[i];
            if(trenutna != prethodna + 1) {
                return 0;
            }
            prethodna = trenutna;
        }
        return 40;
    }

}
