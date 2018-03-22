/* CRITTERS Critter1.java
 * EE422C Project 4 submission by
 * Zack Campbell
 * zcc254
 * Audrey Gan
 * ayg333
 * Slip days used: <0>
 * Spring 2018
 */

package assignment4;

public class Critter1 extends Critter {
    //elephant Critter
    @Override
    public String toString() { return "1"; }

    private static final int GENE_TOTAL = 24;
    private int[] genes = new int[8];
    private int dir;

    public Critter1() {
        for (int k = 0; k < 8; k += 1) {
            genes[k] = GENE_TOTAL / 8;
        }
        dir = Critter.getRandomInt(8);
    }

    public boolean fight(String not_used) { return true; }

    /**
     * Overriding abstract Critter's doTimeStep()
     * Can only walk one space or stand still with 50% chance
     * Loses half as much energy as normal when walking
     * Wins every fight but it takes 1/3 of it’s starting energy to do so
     */
    @Override
    public void doTimeStep() {
        /* take one step forward */
        int doesMove = Critter.getRandomInt(10);
        if(doesMove <= 4){ //only moves with 50% chance
            walk(dir);
            this.setEnergy(this.getEnergy()+(Params.walk_energy_cost/2));    //loses half as much energy as usual when walking
        }

        if (getEnergy() > 150) {
            Critter1 child = new Critter1();
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
    public static void runStats(java.util.List<Critter> critter1) {
        int total_straight = 0;
        int total_left = 0;
        int total_right = 0;
        int total_back = 0;
        for (Object obj : critter1) {
            Critter1 c = (Critter1) obj;
            total_straight += c.genes[0];
            total_right += c.genes[1] + c.genes[2] + c.genes[3];
            total_back += c.genes[4];
            total_left += c.genes[5] + c.genes[6] + c.genes[7];
        }
        System.out.print("" + critter1.size() + " total Critter1    ");
        System.out.print("" + critter1Wins + " total Critter1 wins    ");
        System.out.print("" + total_straight / (GENE_TOTAL * 0.01 * critter1.size()) + "% straight   ");
        System.out.print("" + total_back / (GENE_TOTAL * 0.01 * critter1.size()) + "% back   ");
        System.out.print("" + total_right / (GENE_TOTAL * 0.01 * critter1.size()) + "% right   ");
        System.out.print("" + total_left / (GENE_TOTAL * 0.01 * critter1.size()) + "% left   ");
        System.out.println();
    }
}
