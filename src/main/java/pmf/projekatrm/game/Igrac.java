package pmf.projekatrm.game;

import java.util.ArrayList;
import java.util.Objects;

public class Igrac {
    public static ArrayList<Igrac> sviIgraci = new ArrayList<>();
    private String korisnickoIme;
    private String stanje;

    public Igrac(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
        if (postoji(korisnickoIme)) {
            return;
        }
        this.stanje = "slobodan";
        if (!sviIgraci.contains(this)) {
            sviIgraci.add(this);
        }
    }

    private boolean postoji(String korisnickoIme) {
        for (Igrac i : sviIgraci) {
            if (i.getKorisnickoIme().equals(korisnickoIme)) {
                i.setStanje("slobodan");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Igrac igrac = (Igrac) o;
        return korisnickoIme.equals(igrac.korisnickoIme) && stanje.equals(igrac.stanje);
    }

    @Override
    public int hashCode() {
        return Objects.hash(korisnickoIme, stanje);
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getStanje() {
        return this.stanje;
    }

    public void setStanje(String stanje) {
        this.stanje = stanje;
    }

}
