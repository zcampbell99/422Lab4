package assignment4;

public class Critter3 extends Critter {
    // Rabbit Critter

    /**
     * Override the toString from Critter to have the digit of Critter3
     * @return a string with the number 3
     */
    @Override
    public String toString() { return "3"; }

    private static final int GENE_TOTAL = 24;
    private int[] genes = new int[8];
    private int dir;
    private boolean didRep;

    /**
     * Critter3 Constructor
     * Set the direction randomly, set the start energy, and it has not reproduced yet
     */
    public Critter3() {
        for (int k = 0; k < 8; k += 1) {
            genes[k] = GENE_TOTAL / 8;
        }
        dir = Critter.getRandomInt(8);
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
     * Reproduction requires less energy than normal
     */
    @Override
    public void doTimeStep() {
        if (Critter.getRandomInt(10) <= 3) {                    // More likely to run than walk
            walk(dir);
        } else {
            int temp = Params.run_energy_cost;                      // Running takes half as much energy
            Params.run_energy_cost /= 2;
            run(dir);
            Params.run_energy_cost = temp;
        }
        if (this.getEnergy() >= 25 && !didRep) {
            didRep = true;
            Critter3 child = new Critter3();
            setEnergy(getEnergy()+(getEnergy()/4));
            reproduce(child, Critter.getRandomInt(8));
        } else {
            didRep = false;
        }
    }
    public static void runStats(java.util.List<Critter> critter3) {
        int total_straight = 0;
        int total_left = 0;
        int total_right = 0;
        int total_back = 0;
        for (Object obj : critter3) {
            Critter3 c = (Critter3) obj;
            total_straight += c.genes[0];
            total_right += c.genes[1] + c.genes[2] + c.genes[3];
            total_back += c.genes[4];
            total_left += c.genes[5] + c.genes[6] + c.genes[7];
        }
        System.out.print("" + critter3.size() + " total Critter3    ");
        System.out.print("" + total_straight / (GENE_TOTAL * 0.01 * critter3.size()) + "% straight   ");
        System.out.print("" + total_back / (GENE_TOTAL * 0.01 * critter3.size()) + "% back   ");
        System.out.print("" + total_right / (GENE_TOTAL * 0.01 * critter3.size()) + "% right   ");
        System.out.print("" + total_left / (GENE_TOTAL * 0.01 * critter3.size()) + "% left   ");
        System.out.println();
    }
}