package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Zack Campbell
 * zcc254
 * Audrey Gan
 * ayg333
 * Slip days used: <0>
 * Spring 2018
 */


import java.util.*;
//import java.util.List;
//import java.lang.reflect.*;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
    private static String myPackage;
    private	static List<Critter> population = new java.util.ArrayList<Critter>();
    private static LinkedList<Critter> pop = new LinkedList<Critter>();
    private static List<Critter> babies = new java.util.ArrayList<Critter>();
    private static HashMap<Point, LinkedList<Critter>> grid = new HashMap<Point, LinkedList<Critter>>();
    private static int timestep = 0;

    // Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static java.util.Random rand = new java.util.Random();
    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new java.util.Random(new_seed);
    }


    /* a one-character long string that visually depicts your critter in the ASCII interface */
    public String toString() { return ""; }

    private int energy = 0;
    protected int getEnergy() { return energy; }
    protected void setEnergy(int e) {
        this.energy = e;
    }
    protected int getX() {
        return x_coord;
    }

    protected int getY() {
        return y_coord;
    }

    private int x_coord;
    private int y_coord;
    private int numofmoves = 0; //for walk/run

    /**
     * Moves a critter one step in the given direction
     * @param direction of movement
     */
    protected final void walk(int direction) {
        move(direction,1,++numofmoves);
    }

    /**
     * Moves a critter two steps in the given direction
     * @param direction of movement
     */
    protected final void run(int direction) {
        move(direction,2,++numofmoves);
    }

    /**
     * Core functionality of the move function that deals with energy and fights
     * @param direction of movement
     * @param steps to move
     * @param numofmoves done during one timestep
     */
    private void move(int direction, int steps, int numofmoves) {
        if(steps==1)
            energy -= Params.walk_energy_cost;
        else
            energy -= Params.run_energy_cost;
        if(numofmoves == 1 && alive(this)) {
            Point prev_pos = new Point(x_coord, y_coord);
            move2(direction,steps, this);
            Point new_pos = new Point(x_coord, y_coord);

            if(afterInitialMove) {	//if called in fight()
                for(Map.Entry<Point, LinkedList<Critter>> entry : grid.entrySet()) { // iterate through each occupied position
                    if(new_pos.equals(new Point(entry.getKey().x, entry.getKey().y))) {
                        new_pos = prev_pos;  // if the position is already occupied by a critter don't move this one
                        break;
                    }
                }
            }
            if(!new_pos.equals(prev_pos)) {
                LinkedList<Critter> cac = grid.get(prev_pos);
                cac.remove(this);
                if(cac.size() == 0) {
                    grid.remove(prev_pos);
                }
                if(grid.containsKey(new_pos)) {
                    grid.get(new_pos).add(this); //add to arraylist if position already has a critter
                }
                else {
                    LinkedList<Critter> ac = new LinkedList<Critter>();
                    ac.add(this);
                    grid.put(new_pos, ac); //create new key with an arraylist of the 1 critter
                }
            }

            x_coord = new_pos.x; y_coord = new_pos.y; // change the critter position to the new position
        }
    }

    /**
     * Core functionality of the move function that deals with movement on the grid
     * @param direction of movement
     * @param steps to take
     * @param crit to be moved
     */
    private static void move2(int direction, int steps, Critter crit){
        Point new_pos = new Point(crit.x_coord, crit.y_coord);
        switch(direction) {
            case 0:
                new_pos.x += steps;
                break;
            case 1:
                new_pos.x += steps;
                new_pos.y -= steps;
                break;
            case 2:
                new_pos.y -= steps;
                break;
            case 3:
                new_pos.x -= steps;
                new_pos.y -= steps;
                break;
            case 4:
                new_pos.x -= steps;
                break;
            case 5:
                new_pos.x -= steps;
                new_pos.y += steps;
                break;
            case 6:
                new_pos.y += steps;
                break;
            case 7:
                new_pos.x += steps;
                new_pos.y += steps;
                break;
            default:
                throw new IllegalArgumentException();
        }

        if(new_pos.x >= Params.world_width)
            new_pos.x -= Params.world_width;
        else if (new_pos.x < 0)
            new_pos.x += Params.world_width;
        if(new_pos.y >= Params.world_height)
            new_pos.y -= Params.world_height;
        else if (new_pos.y < 0)
            new_pos.y += Params.world_height;

        crit.x_coord = new_pos.x; crit.y_coord = new_pos.y;
    }

    /**
     * Sets the fields for the given object and adds to the babies arraylist
     * @param offspring of the critter
     * @param direction of movement
     */
    protected final void reproduce(Critter offspring, int direction) {
        if(this.energy < Params.min_reproduce_energy) return;
        offspring.energy = this.energy/2;
        this.energy = this.energy/2 + 1;
        offspring.x_coord = this.x_coord;
        offspring.y_coord = this.y_coord;
        move2(direction, 1, offspring);
        babies.add(offspring);
    }


    public abstract void doTimeStep();
    public abstract boolean fight(String oponent);

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
     * an InvalidCritterException must be thrown.
     * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
     * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
     * an Exception.)
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void makeCritter(String critter_class_name) throws InvalidCritterException {
        critter_class_name = critter_class_name.trim(); //get rid of any whitespace
        try {
            Class<?> critter_class = Class.forName(myPackage + "." + critter_class_name);
            Class<?> critter = Class.forName(myPackage + ".Critter");
            if(critter.isAssignableFrom(critter_class)) {
                Critter critter_instance = (Critter)critter_class.newInstance(); //IllegalAccessException if no nullary constructor

                //prepare critter for simulation, create initial position
                critter_instance.x_coord = getRandomInt(Params.world_width);  // have to use the constrained randomizer
                critter_instance.y_coord = getRandomInt(Params.world_height); //set position
                critter_instance.energy = Params.start_energy; //set energy

                pop.addLast(critter_instance); //should be able to cast to critter

                // Critter is added to grid
                addToGrid(critter_instance);


            }
            else
                throw new InvalidCritterException(critter_class_name); //case when critter_class is not a subclass of critter
        } catch (ClassNotFoundException e) {
            throw new InvalidCritterException(critter_class_name); //case when critter_class doesn't even exist
        } catch (NoClassDefFoundError e) {
            throw new InvalidCritterException(critter_class_name); //case when critter_class is different in case, gives error on case-insensitive systems
        } catch (InstantiationException e) {
            throw new InvalidCritterException(critter_class_name); //case when critter_class isn't concrete
        } catch (IllegalAccessException e) {
            throw new InvalidCritterException(critter_class_name); //case when critter_class's definition can't be accessed, privilege thing? yep
        }
    }

    /**
     * Adds the given critter to the critter grid
     * @param critter_instance to be added
     */
    private static void addToGrid(Critter critter_instance) {
        Point critpos = new Point(critter_instance.x_coord, critter_instance.y_coord);
        if(grid.containsKey(critpos)) {
            grid.get(critpos).add(critter_instance);
        } else {
            LinkedList<Critter> crit = new LinkedList<Critter>();
            crit.add(critter_instance);
            grid.put(critpos, crit);
        }
    }

    /**
     * Gets a list of critters of a specific type.
     * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
        List<Critter> result = new java.util.ArrayList<Critter>();
        try{
            Class<?> critter_class = Class.forName(myPackage + "." +critter_class_name);
            Class<?> critter = Class.forName(myPackage + ".Critter");
            if(critter.isAssignableFrom(critter_class)) {
                for(Critter crit : pop) {
                    if(critter_class.equals(crit.getClass()) || crit.getClass().isInstance(critter_class)){
                        result.add(crit);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new InvalidCritterException(critter_class_name); //case when critter_class doesn't even exist
        } catch (NoClassDefFoundError e) {
            throw new InvalidCritterException(critter_class_name); //case when critter_class is different in case, gives error on case-insensitive systems
        }

        return result;
    }

    /**
     * Prints out how many Critters of each type there are on the board.
     * @param critters List of Critters.
     */
    public static void runStats(List<Critter> critters) {
        System.out.print("" + critters.size() + " critters as follows -- ");
        java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            Integer old_count = critter_count.get(crit_string);
            if (old_count == null) {
                critter_count.put(crit_string,  1);
            } else {
                critter_count.put(crit_string, old_count.intValue() + 1);
            }
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            System.out.print(prefix + s + ":" + critter_count.get(s));
            prefix = ", ";
        }
        System.out.println();
    }

    /* the TestCritter class allows some critters to "cheat". If you want to
     * create tests of your Critter model, you can create subclasses of this class
     * and then use the setter functions contained here.
     *
     * NOTE: you must make sure that the setter functions work with your implementation
     * of Critter. That means, if you're recording the positions of your critters
     * using some sort of external grid or some other data structure in addition
     * to the x_coord and y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {
        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;

        }

        protected void setX_coord(int new_x_coord) {
            Point prev_pos = new Point(this.getX_coord(),this.getY_coord());
            super.x_coord = new_x_coord;
            Point new_pos = new Point(this.getX_coord(),this.getY_coord());

            if(!new_pos.equals(prev_pos)) {
                LinkedList<Critter> cac = grid.get(prev_pos);
                cac.remove(this);
                if(cac.size() == 0) {
                    grid.remove(prev_pos);
                }
                addToGrid(this);
				/*if(grid.containsKey(new_pos)) {
					grid.get(new_pos).add(this); //add to arraylist if position already has a critter
				}
				else {
					LinkedList<Critter> ac = new LinkedList<Critter>();
					ac.add(this);
					grid.put(new_pos, ac); //create new key with an arraylist of the 1 critter
				}*/
            }
        }

        protected void setY_coord(int new_y_coord) {
            Point prev_pos = new Point(this.getX_coord(),this.getY_coord());
            super.y_coord = new_y_coord;
            Point new_pos = new Point(this.getX_coord(),this.getY_coord());

            if(!new_pos.equals(prev_pos)) {
                LinkedList<Critter> cac = grid.get(prev_pos);
                cac.remove(this);
                if(cac.size() == 0) {
                    grid.remove(prev_pos);
                }
                addToGrid(this);
            }
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }


        /*
         * This method getPopulation has to be modified by you if you are not using the population
         * ArrayList that has been provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            population.clear();
            population.addAll(pop);
            return population;
        }

        /*
         * This method getBabies has to be modified by you if you are not using the babies
         * ArrayList that has been provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.  Babies should be added to the general population
         * at either the beginning OR the end of every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
        pop.clear();
        population.clear();
        grid.clear();
    }

    private static boolean afterInitialMove;

    /**
     * Method that controls the overall flow of each timestep,
     * executes the timestep of each critter, and then resolves all issues with fights,
     * rest energy, babies, and dead critters.
     */
    public static void worldTimeStep() {
        timestep++;
        afterInitialMove = false; // for walk/run
        for(Critter crit : pop) {
            //Point crit_pos_before = new Point(crit.x_coord, crit.y_coord);

            crit.numofmoves = 0; //for walk/run
            crit.doTimeStep();

			/*Point crit_pos_after = new Point(crit.x_coord, crit.y_coord);
			if(!(crit_pos_before.equals(crit_pos_after))) {
				ArrayList<Critter> cac = grid.get(crit_pos_before); // how does grid have a key of crit_pos_before?
				cac.remove(crit);
				if(cac.size() == 0) {
					grid.remove(crit_pos_before);
				}
				if(grid.containsKey(crit_pos_after)) {
					grid.get(crit_pos_after).add(crit); //add to arraylist if position already has a critter
				}
				else {
					ArrayList<Critter> ac = new ArrayList<Critter>();
					ac.add(crit);
					grid.put(crit_pos_after, ac); //create new key with an arraylist of the 1 critter
				}
			}*/
        }
        doEncounter(); // After all critters have moved simulate encounter
        updateRestEnergy(); // update energy of all critters

        //try {
        genAlgae();
        //} catch (InvalidCritterException e) {
        //e.printStackTrace();
        //} // generate algae
        pop.addAll(babies); // move all babies to general population
        for(Critter crit : babies) addToGrid(crit);	// move all babies onto grid
        babies.clear();	// clear the babies array for next time-step
        clearTheDead(); // clear all dead critters
        //if(timestep%1000 == 0)
        //System.out.print(timestep + " ");
    }

    /**
     * Used to generate the specified number of algae in Params each timestep
     */
    private static void genAlgae() {//throws InvalidCritterException {
        for(int i = 0; i<Params.refresh_algae_count; i++) {
            //makeCritter("Algae");
            Critter al = new Algae();
            al.x_coord = getRandomInt(Params.world_width);
            al.y_coord = getRandomInt(Params.world_height);
            al.energy = Params.start_energy;
            pop.addLast(al);
            addToGrid(al);
			/*al.setX_coord(getRandomInt(Params.world_width));
			al.setY_coord(getRandomInt(Params.world_height));
			al.setEnergy(Params.start_energy);
			pop.add(al);
			addToGrid(al);*/
        }
    }


    /**
     * Method to do all of the fighting in the critter world.
     * It traverses our grid HashMap that we use to represent the world logically,
     * and executes a fight pairwise with each critter at a certain position,
     * until that position has only 1 critter left.
     */
    private static void doEncounter() {
        afterInitialMove = true; // if walk/run is called after doTimeStep()
        for(Map.Entry<Point, LinkedList<Critter>> entry : grid.entrySet()) {
            LinkedList<Critter> v = entry.getValue();
            while(v.size() > 1 ) {
                //System.out.println(v.size());
                int i = 0;
                Critter A = v.get(i); Critter B = v.get(i+1);
                boolean fightA = A.fight(B.toString()); // might run away
                boolean fightB = B.fight(A.toString()); // might run away
                //if A & B alive and Apos==Bpos
                if(alive(A) && alive(B) && (new Point(A.x_coord, A.y_coord)).equals(new Point(B.x_coord,B.y_coord))) {
                    int rollA = 0; int rollB = 0;
                    if(fightA) rollA = getRandomInt(A.energy);
                    if(fightB) rollB = getRandomInt(B.energy);
                    if(rollA >= rollB)  {
                        A.energy += B.energy/2;
                        B.energy = 0;
                    }
                    else {
                        B.energy += A.energy/2;
                        A.energy = 0;
                    }
                }

				/*if(!(new Point(A.x_coord, A.y_coord)).equals(new Point(B.x_coord,B.y_coord))) {
					LinkedList<Critter> lc1 = grid.get(new Point(A.x_coord, A.y_coord));
					LinkedList<Critter> lc2 = grid.get(new Point(A.x_coord, A.y_coord));
					if(lc1.size() == 1 && lc2.size() == 1)
						break;
				}*/

                if(!alive(A)) {
                    v.remove(A);
                    pop.remove(A);
                }
                if(!alive(B)) {
                    v.remove(B);
                    pop.remove(B);
                }
                //System.out.println(v.size());
            }
        }
    }

    /**
     * Helper method to check if a critter is alive.
     * @param crit The critter to be checked.
     * @return boolean True for alive, and false otherwise.
     */
    private static boolean alive(Critter crit) {
        if(crit.energy <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Used to deduct the rest energy cost of each timestep from the population.
     */
    private static void updateRestEnergy() {
        for(Critter crit : pop) {
            crit.energy -= Params.rest_energy_cost;
        }
    }

    /**
     * Used to clear critters who have no energy left from the population.
     */
    private static void clearTheDead() {
		/*for(int i=0; i<pop.size(); i++) {
			if(!alive(pop.get(i))) {
				pop.remove(i);
				i--;
			}
		}*/
        Iterator<Critter> it = pop.iterator();
        while(it.hasNext()) {
            if(!alive(it.next())) {
                it.remove();
            }
        }
    }

    /*
     * Inner class for a simple 2D point that we use as a key in our HashMap for the grid of critters
     */
    private static class Point {
        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Method used to compare 2 points.
         * Required to make HashMap work properly with our Point keys.
         */
        @Override
        public boolean equals(Object o) {
            Point p = (Point)o;
            return (p.x == x) && (p.y == y);
        }
        /**
         * Method used to hash the 2 int field of our Point class.
         * Required to make HashMap work properly with our Point keys.
         */
        @Override
        public int hashCode() {
            return Objects.hash(x,y);
        }
    }


    /**
     * Prints out the state of the critter world into the console
     */
    public static void displayWorld() {
        for(int i = 0; i < Params.world_height+2;i++) {
            for(int j = 0; j < Params.world_width+2; j++) {
                if(i == 0 || i == Params.world_height+1) {
                    if(j == 0 || j == Params.world_width+1)
                        System.out.print("+");
                    else
                        System.out.print("-");
                }
                else if(j == 0 || j == Params.world_width+1)
                    System.out.print("|");
                else {
                    Point p = new Point(i-1,j-1);
                    if(grid.containsKey(p)) {
                        LinkedList<Critter> ac = grid.get(p);
                        if(ac.size() >= 1) {
                            System.out.print(ac.get(0).toString());
                        }
                    }
                    else
                        System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

}