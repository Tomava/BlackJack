package blackjack;

import java.util.ArrayList;

public class Pelaaja {

    private ArrayList<Kortti> kasi;
    private int maxSumma;
    private int minSumma;
    private boolean assia;

    public Pelaaja() {
        this.kasi = new ArrayList<>();
        maxSumma = 0;
        minSumma = 0;
        assia = false;
    }

    public void lisaaKortti(Kortti kortti) {
        this.kasi.add(kortti);
        if (kortti.getLuku() == "A") {
            assia = true;
        }
        if (!assia) {
            maxSumma += kortti.getArvo();
            minSumma = maxSumma;
        } else {
            maxSumma += kortti.getArvo();
            if (kortti.getLuku() == "A") {
                minSumma += 1;
            } else {
                minSumma += kortti.getArvo();
            }
        }

    }

    public ArrayList<Kortti> getKasi() {
        return kasi;
    }

    public int getMaxSumma() {
        if (maxSumma <= 21) {
            return maxSumma;
        } else {
            return minSumma;
        }
    }

    public String getSummat() {
        if (assia) {
            if (getMaxSumma() == maxSumma) {
                return (this.maxSumma + "/" + this.minSumma);
            } else {
                return (String.valueOf(getMaxSumma()));
            }
        } else {
            return (String.valueOf(getMaxSumma()));
        }
    }

    public boolean yli() {
        return this.minSumma > 21;
    }

}
