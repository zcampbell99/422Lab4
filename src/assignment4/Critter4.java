package assignment4;

public class Critter4 extends Critter {
    // Gorilla Critter

    @Override
    public String toString() { return "4"; }

    private int dir;
    private boolean moved = false;

    /**
     * Critter4 constructor
     */
    public Critter4() {
        dir = Critter.getRandomInt(8);
        setEnergy(Params.start_energy);
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
}
