package pmf.projekatrm.game;

import java.util.Random;

public class Kocka {

    private int vrijednost;

    public Kocka() {
        this.baci();
    }

    public int baci() {
        Random random = new Random();
        this.vrijednost = random.nextInt(6) + 1;
        return this.vrijednost;
    }

    public int getVrijednost() {
        return this.vrijednost;
    }

}
