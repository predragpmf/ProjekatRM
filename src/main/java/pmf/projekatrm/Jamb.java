package pmf.projekatrm;

import java.util.ArrayList;

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
        for (Kocka k: sveKocke) {
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
        for(int i = 0; i < 6; i++) {
            brojVrijednosti[i] = prebroj(i + 1);
            //System.out.println("Broj " + (i + 1) + " je = " + brojVrijednosti[i]);
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

    public int smallStraight() {
        //TODO
        return 0;
    }

    public int largeStraight() {
        //TODO
        return 0;
    }

}
