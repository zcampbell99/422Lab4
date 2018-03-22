/* CRITTERS Critter3.java
 * EE422C Project 4 submission by
 * Zack Campbell
 * zcc254
 * Audrey Gan
 * ayg333
 * Slip days used: <0>
 * Spring 2018
 */

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
            walk(Critter.getRandomInt(8));
        } else {
            int temp = Params.run_energy_cost;                      // Running takes half as much energy
            Params.run_energy_cost /= 2;
            run(Critter.getRandomInt(8));
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
        System.out.println(critter3.size() + " total Critter3s");
        System.out.println("The Critter3s have reproduced a total of " + repCount + " times");
    }
}