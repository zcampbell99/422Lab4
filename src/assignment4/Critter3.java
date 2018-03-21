package assignment4;

public class Critter3 extends Critter {
    // Rabbit Critter

    /**
     * Override the toString from Critter to have the digit of Critter3
     * @return a string with the number 3
     */
    @Override
    public String toString() { return "3"; }

    private int dir;
    private boolean didRep;

    /**
     * Critter3 Constructor
     * Set the direction randomly, set the start energy, and it has not reproduced yet
     */
    public Critter3() {
        dir = Critter.getRandomInt(8);
        setEnergy(Params.start_energy);
        didRep = false;
    }

    /**
     * Determine whether or not the critter will fight the opponent or run
     * @param not_used is the string of the opponent to fight
     * @return true false if it wants to run away
     */
    public boolean fight(String not_used) {
        if (Critter.getRandomInt(10) <= 3) {
            return true;
        }
        return false;
    }

    /**
     * Overriding abstract Critter's doTimeStep()
     * Critter3 is more likely to run than walk
     * It reproduces every other step and reproduction doesn't take as much energy
     * Reproduction requires less energy than
     */
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