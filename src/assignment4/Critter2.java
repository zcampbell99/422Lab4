/* CRITTERS Critter2.java
 * EE422C Project 4 submission by
 * Zack Campbell
 * zcc254
 * Audrey Gan
 * ayg333
 * Slip days used: <0>
 * Spring 2018
 */

package assignment4;

public class Critter2 extends Critter {
    //roadrunner Critter
    @Override
    public String toString() { return "2"; }

    private static final int GENE_TOTAL = 24;
    private int[] genes = new int[8];
    private int dir;

    public Critter2() {
        for (int k = 0; k < 8; k += 1) {
            genes[k] = GENE_TOTAL / 8;
        }
        dir = Critter.getRandomInt(8);
    }

    public boolean fight(String opponent) {
        if (opponent.equals("2")) {
            return false;
        }
        return true;
    }

    /**
     * Overriding abstract Critter's doTimeStep()
     * Can run at half the cost
     * Loses double energy by standing still
     * Reproduces 2 babies at a time
     * Doesn't fight other Critter2's
     */
    @Override
    public void doTimeStep() {
        int doesMove = Critter.getRandomInt(10);
        if(doesMove <= 7){ //only moves with 80% chance
            run(dir);
            this.setEnergy(this.getEnergy()+(Params.run_energy_cost));    //runs at half the cost
        }else{
            this.setEnergy(this.getEnergy()-(Params.rest_energy_cost*2));    //loses twice as much energy when resting
        }

        if (getEnergy() > 2*Params.min_reproduce_energy) {
            Critter2 child = new Critter2();
            for (int k = 0; k < 8; k += 1) {
                child.genes[k] = this.genes[k];
            }
            int g = Critter.getRandomInt(8);
            while (child.genes[g] == 0) {
                g = Critter.getRandomInt(8);
            }
            child.genes[g] -= 1;
            g = Critter.getRandomInt(8);
            child.genes[g] += 1;
            reproduce(child, Critter.getRandomInt(8));

            Critter2 child2 = new Critter2();           //creates a second child each time it reproduces
            for (int k = 0; k < 8; k += 1) {
                child2.genes[k] = this.genes[k];
            }
            int g2 = Critter.getRandomInt(8);
            while (child2.genes[g] == 0) {
                g2 = Critter.getRandomInt(8);
            }
            child.genes[g2] -= 1;
            g2 = Critter.getRandomInt(8);
            child.genes[g2] += 1;
            reproduce(child2, Critter.getRandomInt(8));
        }

        /* pick a new direction based on our genes */
        int roll = Critter.getRandomInt(GENE_TOTAL);
        int turn = 0;
        while (genes[turn] <= roll) {
            roll = roll - genes[turn];
            turn = turn + 1;
        }
        assert(turn < 8);

        dir = (dir + turn) % 8;
    }
    public static void runStats(java.util.List<Critter> critter2) {
        int total_straight = 0;
        int total_left = 0;
        int total_right = 0;
        int total_back = 0;
        for (Object obj : critter2) {
            Critter2 c = (Critter2) obj;
            total_straight += c.genes[0];
            total_right += c.genes[1] + c.genes[2] + c.genes[3];
            total_back += c.genes[4];
            total_left += c.genes[5] + c.genes[6] + c.genes[7];
        }
        System.out.print("" + critter2.size() + " total Critter2    ");
        System.out.print("" + critter2Deaths + " total Critter2 deaths    ");
        System.out.print("" + total_straight / (GENE_TOTAL * 0.01 * critter2.size()) + "% straight   ");
        System.out.print("" + total_back / (GENE_TOTAL * 0.01 * critter2.size()) + "% back   ");
        System.out.print("" + total_right / (GENE_TOTAL * 0.01 * critter2.size()) + "% right   ");
        System.out.print("" + total_left / (GENE_TOTAL * 0.01 * critter2.size()) + "% left   ");
        System.out.println();
    }

}
