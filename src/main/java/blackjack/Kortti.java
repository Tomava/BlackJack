package blackjack;

public class Kortti {

    private Maa maa;
    private int luku;

    public Kortti(Maa maa, int luku) {
        this.maa = maa;
        this.luku = luku;
    }

    public String getLuku() {
        if (luku == 1) {
            return "A";
        }
        else if (luku == 11) {
            return "J";
        }
        else if (luku == 12) {
            return "Q";
        }
        else if (luku == 13) {
            return "K";
        }
        else {
            return String.valueOf(luku);
        }
    }

    public Maa getMaa() {
        return maa;
    }
    
    public int getArvo() {
        if (this.luku > 10) {
            return 10;
        }
        if (this.luku == 1) {
            return 11;
        }
        else {
            return this.luku;
        }
    }

    @Override
    public String toString() {
        return maa.getCode() + "" + getLuku();
    }
}
