package blackjack;

public class Jakaja extends Pelaaja {
    private int maks;

    public Jakaja(int maks) {
        super();
        this.maks = maks;
    }
    
    public boolean canHit() {
        System.out.println("MAKS=" + this.maks);
        System.out.println("getSumma()=" + getMaxSumma());
        return (getMaxSumma() < this.maks);
    }
}
