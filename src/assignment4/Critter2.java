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

    public boolean fight(String not_used) { return true; }

    /**
     * Overriding abstract Critter's doTimeStep()
     * Can run 2 times in one time step at the cost of 1 run
     * Loses double energy by standing still
     * Reproduces 2 babies at a time
     */
    @Override
    public void doTimeStep() {
        int doesMove = Critter.getRandomInt(10);
        if(doesMove <= 7){ //only moves with 80% chance
            run(dir);
            this.setEnergy(this.getEnergy()+(Params.run_energy_cost));    //runs twice at the cost of only a single run
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

}
