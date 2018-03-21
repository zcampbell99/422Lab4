package assignment4;

public class Critter3 extends Critter {
    // Rabbit Critter
    @Override
    public String toString() { return "3"; }

    private static final int GENE_TOTAL = 24;
    private int dir;

    public Critter3() {
        dir = Critter.getRandomInt(8);
        setEnergy(Params.start_energy);
    }

    public boolean fight(String opponent) {
        if (Critter.getRandomInt(10) <= 3) {
            return true;
        }
        return false;
    }

    @Override
    public void doTimeStep() {

    }
}
