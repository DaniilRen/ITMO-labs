package model.tools;

public class Shoes {
    private final ShoeType color;
    private final Buckle buckle;

    public Shoes(ShoeType color) {
        this.color = color;
        this.buckle = new Buckle("полумесяц со звездой");
    }

    @Override
    public String toString() {
        return color.toString() + " туфли с " + buckle;
    }
}