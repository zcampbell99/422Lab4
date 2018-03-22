package assignment4;

public class Critter4 extends Critter {
    // Gorilla Critter

    @Override
    public String toString() { return "4"; }

    private static final int GENE_TOTAL = 24;
    private int[] genes = new int[8];
    private int dir;

    /**
     * Critter4 Constructor
     * Set the direction randomly, set the start energy, and it has not reproduced yet
     */
    public Critter4() {
        for (int k = 0; k < 8; k += 1) {
            genes[k] = GENE_TOTAL / 8;
        }
        dir = Critter.getRandomInt(8);
    }

    /**
     * Determine if Critter4 should fight another critter
     * If the opponent is another Critter4 then it wont fight
     * @param opponent is the critter that this critter has encountered
     * @return true if this critter wants to fight
     */
    public boolean fight(String opponent) {
        if (opponent.equals("4") || opponent.equals("C")) {
            return false;
        }
        return true;
    }

    /**
     * Override the abstract doTimeStep() method in Critter
     * Will only move half of the time
     * When it walks, it walks 3 spaces with the same cost as a run
     * Energy to reproduce is 100
     */
    @Override
    public void doTimeStep() {
        int moving = Critter.getRandomInt(10);
        if (moving <= 4) {
            setEnergy(getEnergy()-Params.rest_energy_cost);
        } else if (moving <= 7) {
            int temp = Params.walk_energy_cost;
            Params.walk_energy_cost = Params.run_energy_cost/3;
            run(Critter.getRandomInt(8));
//            walk(Critter.getRandomInt(8));
            walk(Critter.getRandomInt(8));
            Params.walk_energy_cost = temp;
        } else {
            run(Critter.getRandomInt(8));
        }
        if (getEnergy() >= 100) {
            Critter4 child = new Critter4();
            reproduce(child, Critter.getRandomInt(8));
        }
    }
    public static void runStats(java.util.List<Critter> critter4) {
        int total_straight = 0;
        int total_left = 0;
        int total_right = 0;
        int total_back = 0;
        for (Object obj : critter4) {
            Critter4 c = (Critter4) obj;
            total_straight += c.genes[0];
            total_right += c.genes[1] + c.genes[2] + c.genes[3];
            total_back += c.genes[4];
            total_left += c.genes[5] + c.genes[6] + c.genes[7];
        }
        System.out.print("" + critter4.size() + " total Critter4    ");
        System.out.print("" + total_straight / (GENE_TOTAL * 0.01 * critter4.size()) + "% straight   ");
        System.out.print("" + total_back / (GENE_TOTAL * 0.01 * critter4.size()) + "% back   ");
        System.out.print("" + total_right / (GENE_TOTAL * 0.01 * critter4.size()) + "% right   ");
        System.out.print("" + total_left / (GENE_TOTAL * 0.01 * critter4.size()) + "% left   ");
        System.out.println();
    }
}
