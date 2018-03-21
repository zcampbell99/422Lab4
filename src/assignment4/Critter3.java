package assignment4;

public class Critter3 extends Critter {
    // Rabbit Critter
    @Override
    public String toString() { return "3"; }

    private static final int GENE_TOTAL = 24;
    private int dir;
    private boolean didRep;

    public Critter3() {
        dir = Critter.getRandomInt(8);
        setEnergy(Params.start_energy);
        didRep = false;
    }

    public boolean fight(String opponent) {
        if (Critter.getRandomInt(10) <= 3) {
            return true;
        }
        return false;
    }

    @Override
    public void doTimeStep() {
        if (Critter.getRandomInt(10) <= 3) {                    // More likely to run than walk
            updateLoc(dir, 1, ++numMoves);
        } else {
            int temp = Params.run_energy_cost;                      // Running takes half as much energy
            Params.run_energy_cost /= 2;
            run(dir);
            Params.run_energy_cost = temp;
        }
        if (this.getEnergy() >= Params.min_reproduce_energy/2 && !didRep) {
            didRep = true;
            Critter3 child = new Critter3();
            setEnergy(getEnergy()+(getEnergy()/4));
            reproduce(child, Critter.getRandomInt(8));
        } else {
            didRep = false;
        }
    }
}