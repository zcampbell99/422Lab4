package assignment4;

public class Critter3 extends Critter {
    @Override
    public String toString() { return "3"; }

    private static final int GENE_TOTAL = 24;
    private int dir;

    public Critter3() {
        dir = Critter.getRandomInt(8);
    }

    public boolean fight(String not_used) { return true; }

    @Override
    public void doTimeStep() {

    }
}