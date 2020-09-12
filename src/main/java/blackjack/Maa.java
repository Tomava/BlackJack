package blackjack;

public enum Maa {
    Hertta("\u2661"),
    Ruutu("\u2662"),
    Pata("\u2660"),
    Risti("\u2663");

    private String code;

    Maa(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
