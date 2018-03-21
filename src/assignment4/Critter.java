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


import java.awt.*;
import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;


/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
    private static String myPackage;
    private	static List<Critter> population = new java.util.ArrayList<Critter>();
    private static List<Critter> babies = new java.util.ArrayList<Critter>();
    private static HashMap<Point, LinkedList<Critter>> grid = new HashMap<Point, LinkedList<Critter>>();
    private static boolean initialMove;

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

    private int x_coord;
    private int y_coord;
    protected static int numMoves = 0;

    protected int getX() {
        return x_coord;
    }

    protected int getY() {
        return y_coord;
    }

    protected void setX(int x) {
        this.x_coord = x;
    }

    protected void setY(int y) {
        this.y_coord = y;
    }

    /**
     * Updates the location and energy of the critter when moving
     * @param direction is the direction that the critter moves in
     * @param steps is the number of steps the critter takes (walk or run)
     * @param numofmoves is the number of moves the critter has made in one time step
     */
    protected void updateLoc(int direction, int steps, int numofmoves) {
        if(steps==1)
            energy -= Params.walk_energy_cost;  //subtracts energy depending on if critter is walking or running
        else
            energy -= Params.run_energy_cost;
        if(numofmoves == 1 && isAlive(this)) {
            Point prev_pos = new Point(x_coord, y_coord);
            move(direction,steps, this);
            Point new_pos = new Point(x_coord, y_coord);

            if(initialMove) {	//if called in fight()
                for(Map.Entry<Point, LinkedList<Critter>> entry : grid.entrySet()) { // iterate through each occupied position
                    if(new_pos.equals(new Point(entry.getKey().x, entry.getKey().y))) {
                        new_pos = prev_pos;  // if the position is already occupied by a critter don't move this one
                        break;
                    }
                }
            }
            if(!new_pos.equals(prev_pos)) {
                LinkedList<Critter> oldLoc = grid.get(prev_pos);
                oldLoc.remove(this);
                if(oldLoc.size() == 0) {
                    grid.remove(prev_pos);
                }
                if(grid.containsKey(new_pos)) {
                    grid.get(new_pos).add(this); //add to arraylist if position already has a critter
                }
                else {
                    LinkedList<Critter> newLoc = new LinkedList<Critter>();
                    newLoc.add(this);
                    grid.put(new_pos, newLoc); //create new key with an arraylist of the 1 critter
                }
            }
            x_coord = new_pos.x; y_coord = new_pos.y; // change the critter position to the new position
        }
    }

    /**
     * Change the coordinates of the critter to an adjacent location
     * @param direction is the direction in which to place the critter adjacent to it's current position
     */
    private static void move(int direction, int steps, Critter crit){
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

        if(new_pos.x > Params.world_width-1)
            new_pos.x -= Params.world_width;
        else if (new_pos.x < 0)
            new_pos.x += Params.world_width;
        if(new_pos.y > Params.world_height-1)
            new_pos.y -= Params.world_height;
        else if (new_pos.y < 0)
            new_pos.y += Params.world_height;

        crit.x_coord = new_pos.x;
        crit.y_coord = new_pos.y;
    }

    /**
     * Has the critter walk in the specified direction
     * @param direction is the direction in which the critter will move on the grid
     */
    protected final void walk(int direction) {
        updateLoc(direction, 1, ++numMoves);
    }

    /**
     * Has the critter run in the specified direction (two steps instead of one)
     * @param direction is the direction in which the critter will move on the grid
     */
    protected final void run(int direction) {
        updateLoc(direction,2,++numMoves);
    }

    /**
     * Creates children of the specified critter and puts them on the grid if they have enough energy
     * @param offspring is the child whose energy and location will be set
     * @param direction is the direction from the parent critter in which the child will be placed
     */
    protected final void reproduce(Critter offspring, int direction) {
        if (Params.min_reproduce_energy <= energy) {
            if (energy % 2 == 1) {							// Set the mother and baby's energy
                offspring.setEnergy((int)((.5*energy)-0.5));
                setEnergy((int)((.5*energy)+0.5));
            } else {
                offspring.setEnergy((int)(.5*energy));
                setEnergy((int)(.5*energy));
            }
            switch (direction) {							// Set the baby's coordinates
                case 0:                // East
                    if (x_coord == Params.world_width - 1)
                        offspring.x_coord = 0;
                    else
                        offspring.x_coord = x_coord + 1;
                    offspring.y_coord = y_coord;
                    break;
                case 1:                // NorthEast
                    if (x_coord == Params.world_width - 1)
                        offspring.x_coord = 0;
                    else
                        offspring.x_coord = x_coord + 1;
                    if (y_coord == 0)
                        offspring.y_coord = Params.world_height - 1;
                    else
                        offspring.y_coord = y_coord + 1;
                    break;
                case 2:                // North
                    if (y_coord == 0)
                        offspring.y_coord = Params.world_height - 1;
                    else
                        offspring.y_coord = y_coord + 1;
                    offspring.x_coord = x_coord;
                    break;
                case 3:                // NorthWest
                    if (x_coord == 0)
                        offspring.x_coord = Params.world_width - 1;
                    else
                        offspring.x_coord = x_coord - 1;
                    if (y_coord == 0)
                        offspring.y_coord = Params.world_height - 1;
                    else
                        offspring.y_coord = y_coord + 1;
                    break;
                case 4:                // West
                    if (x_coord == 0)
                        offspring.x_coord = Params.world_width - 1;
                    else
                        offspring.x_coord = x_coord - 1;
                    offspring.y_coord = y_coord;
                    break;
                case 5:                // Southwest
                    if (x_coord == 0)
                        offspring.x_coord = Params.world_width - 1;
                    else
                        offspring.x_coord = x_coord - 1;
                    if (y_coord == Params.world_height - 1)
                        offspring.y_coord = 0;
                    else
                        offspring.y_coord = y_coord - 1;
                    break;
                case 6:                // South
                    if (y_coord == Params.world_height - 1)
                        offspring.y_coord = 0;
                    else
                        offspring.y_coord = y_coord - 1;
                    offspring.x_coord = x_coord;
                    break;
                case 7:                // SouthEast
                    if (x_coord == Params.world_width - 1)
                        offspring.x_coord = 0;
                    else
                        offspring.x_coord = x_coord + 1;
                    if (y_coord == Params.world_height - 1)
                        offspring.y_coord = 0;
                    else
                        offspring.y_coord = y_coord - 1;
                    break;
            }
            CritterWorld.babyList.add(offspring);
        }
    }

    public abstract void doTimeStep();
    public abstract boolean fight(String opponent);

    /**
     * Decides the outcome of two critters who come into contact on the grid (run away or fight)
     * @param stackOfCritters is the critters who appear at the same location on the grid
     */
    public static void encounter(LinkedList<Critter> stackOfCritters) {
        initialMove = true;
        while(stackOfCritters.size()>1){
            int i = 0;
            Critter challenger = stackOfCritters.get(i);
            Critter enemy = stackOfCritters.get(i+1);
            boolean Afight = true;
            boolean Bfight = true;
            if (!challenger.fight(enemy.toString())) {  //A doesn't want to fight and will try and walk/run away
                Afight = false;
                int walkDir = findAdjDir(challenger.x_coord, challenger.y_coord);
                if (walkDir != -1) {
                    if (Critter.getRandomInt(10) <= 4 && !challenger.toString().equals("3")) {
                        challenger.walk(walkDir);
                    } else {
                        challenger.run(walkDir);
                    }
                }
            }
            if (!enemy.fight(challenger.toString())) {  //B doesn't want to fight and will try and walk/run away
                Bfight = false;
                int walkDir = findAdjDir(enemy.x_coord, enemy.y_coord);
                if (walkDir != -1)
                    if (Critter.getRandomInt(10) <= 4 && !enemy.toString().equals("3")) {
                        enemy.walk(walkDir);
                    } else {
                        enemy.run(walkDir);
                    }
            }
            if (challenger.getEnergy() > 0 && enemy.getEnergy() > 0 && challenger.x_coord == enemy.x_coord && challenger.y_coord == enemy.y_coord) {
                if(enemy.toString().equals("1")){
                    int winEnergy = (challenger.getEnergy()/2);
                    enemy.setEnergy(((enemy.getEnergy()*2)/3)+winEnergy); //elephant loses a third of their energy but gains half of opponent's energy
                    challenger.setEnergy(0); //opponent is defeated
                }else if(challenger.toString().equals("1")){
                    int winEnergy = (enemy.getEnergy()/2);
                    challenger.setEnergy(((challenger.getEnergy()*2)/3)+winEnergy); //elephant loses a third of their energy but gains half of opponent's energy
                    enemy.setEnergy(0); //opponent is defeated
                } else if (Afight && Bfight){
                    if (Critter.getRandomInt(challenger.energy) >= Critter.getRandomInt(enemy.getEnergy())) {    // Critter A wins
                        challenger.setEnergy((int)(challenger.getEnergy() + (.5*enemy.getEnergy())));
                        enemy.setEnergy(0);
                    } else {
                        enemy.setEnergy((int)(enemy.getEnergy() + (.5*challenger.getEnergy())));
                        challenger.setEnergy(0);
                    }
                } else if (!Afight && Bfight) {
                    if (Critter.getRandomInt(enemy.getEnergy()) > 0) {
                        enemy.setEnergy((int)(enemy.getEnergy() + (.5*challenger.getEnergy())));
                        challenger.setEnergy(0);
                    } else {
                        challenger.setEnergy((int)(challenger.getEnergy() + (.5*enemy.getEnergy())));
                        enemy.setEnergy(0);
                    }
                } else if (!Bfight && Afight) {
                    if (Critter.getRandomInt(challenger.energy) > 0) {
                        challenger.setEnergy((int) (challenger.getEnergy() + (.5 * enemy.getEnergy())));
                        enemy.setEnergy(0);
                    } else {
                        enemy.setEnergy((int) (enemy.getEnergy() + (.5 * challenger.getEnergy())));
                        challenger.setEnergy(0);
                    }
                }
            }if(!isAlive(challenger)){  //remove dead critters from grid and critterList
                stackOfCritters.remove(challenger);
                CritterWorld.critterList.remove(challenger);
            }if(!isAlive(enemy)){
                stackOfCritters.remove(enemy);
                CritterWorld.critterList.remove(enemy);
            }
        }
    }

    /**
     * Find the direction in which the baby critter will be placed adjacent to the parent
     * @param x_OG is the x coordinate of the parent
     * @param y_OG is the y coordinate of the parent
     * @return the direction in which you want to place the critter
     */
    private static int findAdjDir(int x_OG, int y_OG) {
        int a = 0, b = 0, c = 0, d = 0, e = 0, f = 0, g = 0, h = 0;
        if (x_OG == Params.world_width-1 && y_OG == Params.world_height-1) {		// Bottom right corner
            for (Critter crt : CritterWorld.critterList) {
                if (crt.x_coord == 0 && crt.y_coord == Params.world_height-2)
                    h = 1;
                if (crt.x_coord == 0 && crt.y_coord == Params.world_height-1)
                    a = 1;
                if (crt.x_coord == 0 && crt.y_coord == 0)
                    b = 1;
                if (crt.x_coord == x_OG && crt.y_coord == Params.world_height-2)
                    g = 1;
                if (crt.x_coord == x_OG && crt.y_coord == 0)
                    c = 1;
                if (crt.x_coord == Params.world_width-2 && crt.y_coord == Params.world_height-2)
                    f = 1;
                if (crt.x_coord == Params.world_width-2 && crt.y_coord == Params.world_height-1)
                    e = 1;
                if (crt.x_coord == Params.world_width-2 && crt.y_coord == 0)
                    d = 1;
            }
        }
        if (x_OG == Params.world_width-1 && y_OG == 0) {							// Top right corner
            for (Critter crt : CritterWorld.critterList) {
                if (crt.x_coord == 0 && crt.y_coord == 1)
                    b = 1;
                if (crt.x_coord == 0 && crt.y_coord == Params.world_height-1)
                    h = 1;
                if (crt.x_coord == 0 && crt.y_coord == 0)
                    a = 1;
                if (crt.x_coord == x_OG && crt.y_coord == Params.world_height-1)
                    g = 1;
                if (crt.x_coord == x_OG && crt.y_coord == 1)
                    c = 1;
                if (crt.x_coord == Params.world_width-2 && crt.y_coord == 1)
                    d = 1;
                if (crt.x_coord == Params.world_width-2 && crt.y_coord == Params.world_height-1)
                    f = 1;
                if (crt.x_coord == Params.world_width-2 && crt.y_coord == 0)
                    e = 1;
            }
        }
        if (x_OG == 0 && y_OG == 0) {												// Top left corner
            for (Critter crt : CritterWorld.critterList) {
                if (crt.x_coord == 1 && crt.y_coord == 1)
                    b = 1;
                if (crt.x_coord == 1 && crt.y_coord == Params.world_height-1)
                    h = 1;
                if (crt.x_coord == 1 && crt.y_coord == 0)
                    a = 1;
                if (crt.x_coord == x_OG && crt.y_coord == Params.world_height-1)
                    g = 1;
                if (crt.x_coord == x_OG && crt.y_coord == 1)
                    c = 1;
                if (crt.x_coord == Params.world_width-1 && crt.y_coord == 1)
                    d = 1;
                if (crt.x_coord == Params.world_width-1 && crt.y_coord == Params.world_height-1)
                    f = 1;
                if (crt.x_coord == Params.world_width-1 && crt.y_coord == 0)
                    e = 1;
            }
        }
        if (x_OG == 0 && y_OG == Params.world_height-1) {						// Bottom left corner
            for (Critter crt : CritterWorld.critterList) {
                if (crt.x_coord == 1 && crt.y_coord == 0)
                    b = 1;
                if (crt.x_coord == 1 && crt.y_coord == Params.world_height-2)
                    h = 1;
                if (crt.x_coord == 1 && crt.y_coord == Params.world_height-1)
                    a = 1;
                if (crt.x_coord == x_OG && crt.y_coord == Params.world_height-2)
                    g = 1;
                if (crt.x_coord == x_OG && crt.y_coord == 0)
                    c = 1;
                if (crt.x_coord == Params.world_width-1 && crt.y_coord == 0)
                    d = 1;
                if (crt.x_coord == Params.world_width-1 && crt.y_coord == Params.world_height-2)
                    f = 1;
                if (crt.x_coord == Params.world_width-1 && crt.y_coord == Params.world_height-1)
                    e = 1;
            }
        }
        if (y_OG == 0 && x_OG != 0 && x_OG != Params.world_width-1) {					// Top edge
            for (Critter crt : CritterWorld.critterList) {
                if (crt.x_coord == x_OG+1 && crt.y_coord == 1)
                    b = 1;
                if (crt.x_coord == x_OG-1 && crt.y_coord == 1)
                    d = 1;
                if (crt.x_coord == x_OG && crt.y_coord == 1)
                    c = 1;
                if (crt.x_coord == x_OG && crt.y_coord == Params.world_height-1)
                    g = 1;
                if (crt.x_coord == x_OG-1 && crt.y_coord == 0)
                    e = 1;
                if (crt.x_coord == x_OG-1 && crt.y_coord == Params.world_height-1)
                    f = 1;
                if (crt.x_coord == x_OG+1 && crt.y_coord == 0)
                    a = 1;
                if (crt.x_coord == x_OG+1 && crt.y_coord == Params.world_height-1)
                    h = 1;
            }
        }
        if (y_OG == Params.world_height-1 && x_OG != 0 && x_OG != Params.world_width-1) {		// Bottom edge
            for (Critter crt : CritterWorld.critterList) {
                if (crt.x_coord == x_OG+1 && crt.y_coord == 0)
                    b = 1;
                if (crt.x_coord == x_OG-1 && crt.y_coord == 0)
                    d = 1;
                if (crt.x_coord == x_OG && crt.y_coord == 0)
                    c = 1;
                if (crt.x_coord == x_OG && crt.y_coord == Params.world_height-2)
                    g = 1;
                if (crt.x_coord == x_OG-1 && crt.y_coord == Params.world_height-1)
                    e = 1;
                if (crt.x_coord == x_OG-1 && crt.y_coord == Params.world_height-2)
                    f = 1;
                if (crt.x_coord == x_OG+1 && crt.y_coord == Params.world_height-1)
                    a = 1;
                if (crt.x_coord == x_OG+1 && crt.y_coord == Params.world_height-2)
                    h = 1;
            }
        }
        if (x_OG == 0 && y_OG != 0 && y_OG != Params.world_height-1) {		// Left edge
            for (Critter crt : CritterWorld.critterList) {
                if (crt.x_coord == 1 && crt.y_coord == y_OG)
                    a = 1;
                if (crt.x_coord == Params.world_width-1 && crt.y_coord == y_OG)
                    e = 1;
                if (crt.x_coord == 0 && crt.y_coord == y_OG+1)
                    c = 1;
                if (crt.x_coord == 1 && crt.y_coord == y_OG+1)
                    b = 1;
                if (crt.x_coord == Params.world_width-1 && crt.y_coord == y_OG+1)
                    d = 1;
                if (crt.x_coord == 0 && crt.y_coord == y_OG-1)
                    g = 1;
                if (crt.x_coord == 1 && crt.y_coord == y_OG-1)
                    h = 1;
                if (crt.x_coord == Params.world_width-1 && crt.y_coord == y_OG-1)
                    f = 1;
            }
        }
        if (x_OG == Params.world_width-1 && y_OG != 0 && y_OG != Params.world_height-1) {		// Right edge
            for (Critter crt : CritterWorld.critterList) {
                if (crt.x_coord == Params.world_width-2 && crt.y_coord == y_OG)
                    e = 1;
                if (crt.x_coord == 0 && crt.y_coord == y_OG)
                    a = 1;
                if (crt.x_coord == Params.world_width-1 && crt.y_coord == y_OG+1)
                    c = 1;
                if (crt.x_coord == 0 && crt.y_coord == y_OG+1)
                    b = 1;
                if (crt.x_coord == Params.world_width-2 && crt.y_coord == y_OG+1)
                    d = 1;
                if (crt.x_coord == Params.world_width-1 && crt.y_coord == y_OG-1)
                    g = 1;
                if (crt.x_coord == 0 && crt.y_coord == y_OG-1)
                    h = 1;
                if (crt.x_coord == Params.world_width-2 && crt.y_coord == y_OG-1)
                    f = 1;
            }
        }
        if (x_OG != 0 && x_OG != Params.world_width-1 && y_OG != 0 && y_OG != Params.world_height-1) {		// In the middle
            for (Critter crt : CritterWorld.critterList) {
                if (crt.x_coord == x_OG+1 && crt.y_coord == y_OG)
                    a = 1;
                if (crt.x_coord == x_OG-1 && crt.y_coord == y_OG)
                    e = 1;
                if (crt.x_coord == x_OG && crt.y_coord == y_OG+1)
                    c = 1;
                if (crt.x_coord == x_OG+1 && crt.y_coord == y_OG+1)
                    b = 1;
                if (crt.x_coord == x_OG-1 && crt.y_coord == y_OG+1)
                    d = 1;
                if (crt.x_coord == x_OG && crt.y_coord == y_OG-1)
                    g = 1;
                if (crt.x_coord == x_OG+1 && crt.y_coord == y_OG-1)
                    h = 1;
                if (crt.x_coord == x_OG-1 && crt.y_coord == y_OG-1)
                    f = 1;
            }
        }
        List<Integer> directions = new ArrayList<>();
        if (a == 0)
            directions.add(0);
        if (b == 0)
            directions.add(7);
        if (c == 0)
            directions.add(6);
        if (d == 0)
            directions.add(5);
        if (e == 0)
            directions.add(4);
        if (f == 0)
            directions.add(3);
        if (g == 0)
            directions.add(2);
        if (h == 0)
            directions.add(1);
        if (directions.size() == 0)
            return -1;
        int random = getRandomInt(directions.size());
        return directions.get(random);					// Choose a random empty location to move to
    }

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
            if (critter.isAssignableFrom(critter_class)) {
                Critter critter_instance = (Critter) critter_class.newInstance();    //IllegalAcessException if no nullary constructor
                //set critter initial position
                critter_instance.x_coord = getRandomInt(Params.world_width);    //set position with constrained randomizer
                critter_instance.y_coord = getRandomInt(Params.world_height);
                critter_instance.setEnergy(Params.start_energy);
                if(critter_class_name.equals("Algae")){
                    CritterWorld.numAlgae++;
                }
                CritterWorld.critterList.add(critter_instance); //critter is added to the critter list
                addToGrid(critter_instance);    //critter is added to the grid
            } else {
                throw new InvalidCritterException(critter_class_name);
            }
        }
        catch (ClassNotFoundException e){
            throw new InvalidCritterException(critter_class_name);
        }catch (NoClassDefFoundError e){
            throw new InvalidCritterException(critter_class_name);
        }catch (InstantiationException e){
            throw new InvalidCritterException(critter_class_name);
        }catch (IllegalAccessException e){
            throw new InvalidCritterException(critter_class_name);
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
                for(Critter crit : CritterWorld.critterList) {
                    if(critter_class.equals(crit.getClass()) || crit.getClass().isInstance(critter_class)){
                        result.add(crit);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new InvalidCritterException(critter_class_name); //case when critter_class doesn't exist
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

    /** the TestCritter class allows some critters to "cheat". If you want to
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
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
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
    public static void clearWorld() {		// Clear the critters and babies
        CritterWorld.critterList.clear();
        CritterWorld.babyList.clear();
//		population.clear();
        grid.clear();
    }

    /**
     * Clear the critter list and grid of all dead critters
     */
    public static void clearDead() {
        Iterator<Critter> it = CritterWorld.critterList.iterator();
        while(it.hasNext()) {
            if(!isAlive(it.next())) {
                it.remove();
            }
        }
    }

    /**
     * Has all the critters do doTimeStep and resolve any encounters. Updates all critter states and clears the dead. Replenishes Algae
     */
    public static void worldTimeStep() {
        initialMove = false;
        for (Critter c : CritterWorld.critterList) {                // Move every critter
            c.doTimeStep();
        }
        for (Map.Entry<Point, LinkedList<Critter>> c : grid.entrySet()) {      // Resolve all encounters
            if (c.getValue().size() > 1) {
                encounter(c.getValue());
            }
        }
        for (Critter c : CritterWorld.babyList) {                  // Babies are now adults
            CritterWorld.critterList.add(c);
        }
        CritterWorld.babyList.clear();                        // Clear the dead
        clearDead();
        for (int i = CritterWorld.numAlgae; i < Params.refresh_algae_count; i++) {  // Refresh algae
            try {
                makeCritter("Algae");
            } catch (InvalidCritterException e) {
                System.out.println("error processing: " + e.offending_class);
            }
        }
    }

    /**
     * Prints a display of the grid world
     */
    public static void displayWorld() {
        System.out.print("+");
        for (int i = 0; i < Params.world_width; i++) {
            System.out.print("-");
        }
        System.out.println("+");
        for (int i = 0; i < Params.world_height; i++) {
            System.out.print("|");
            for (int j = 0; j < Params.world_width; j++) {
                if (CritterWorld.findCritter(j, i)) {
                    System.out.print(CritterWorld.getFoundCritter(j,i).toString());
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println("|");
        }
        System.out.print("+");
        for (int i = 0; i < Params.world_width; i++) {
            System.out.print("-");
        }
        System.out.println("+");
    }

    /**
     * Adds the critter instance to the world and sets its location
     * @param critter_instance is the critter to be added to the world
     */
    private static void addToGrid(Critter critter_instance){
        Point critpos = new Point(critter_instance.x_coord, critter_instance.y_coord);
        if(grid.containsKey(critpos)){
            grid.get(critpos).add(critter_instance);    //if the position is already occupied on the grid, just add critter to list of critters in position
        }else{
            LinkedList<Critter> crit = new LinkedList<Critter>();   //if the position is empty, add the position and critter to the grid
            crit.add(critter_instance);
            grid.put(critpos,crit);
        }
    }

    /**
     * Checks to see if the specified critter is alive (energy > 0)
     * @param crit is the critter to be checked
     * @return true if the critter is alive and false if dead
     */
    private static boolean isAlive(Critter crit) {
        if(crit.energy <= 0) {
            return false;
        }
        return true;
    }
}