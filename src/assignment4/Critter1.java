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
     * Can only walk one space or stand still
     * Loses half as much energy as normal by walking and standing
     * Wins every fight but it takes 1/3 of it’s starting energy to do so
     */
    @Override
    public void doTimeStep() {
        /* take one step forward */
        int doesMove = Critter.getRandomInt(10);
        if(doesMove <= 4){ //only moves with 50% chance
            walk(dir);
            this.setEnergy(this.getEnergy()+(Params.walk_energy_cost/2));    //loses half as much energy as usual when walking
        }else{
            this.setEnergy(this.getEnergy()-(Params.rest_energy_cost/2));    //loses half as much energy as usual when resting
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

}
