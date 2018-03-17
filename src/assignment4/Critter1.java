package assignment4;

public class Critter1 extends Critter {

    @Override
    public String toString() { return "1"; }

    private static final int GENE_TOTAL = 24;
    private int dir;

    public Critter1() {
        dir = Critter.getRandomInt(8);
    }

    public boolean fight(String not_used) { return true; }

    @Override
    public void doTimeStep() {

    }

}
