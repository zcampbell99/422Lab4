package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */


import java.awt.*;
import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
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
	
	protected final void walk(int direction) {
		switch (direction) {
			case 0:				// East
				if (x_coord == Params.world_width-1)
					x_coord = 0;
				else
					x_coord++;
				break;
			case 1:				// NorthEast
				if (x_coord == Params.world_width-1)
					x_coord = 0;
				else
					x_coord++;
				if (y_coord == 0)
					y_coord = Params.world_height-1;
				else
					y_coord++;
				break;
			case 2:				// North
				if (y_coord == 0)
					y_coord = Params.world_height-1;
				else
					y_coord++;
				break;
			case 3:				// NorthWest
				if (x_coord == 0)
					x_coord = Params.world_width-1;
				else
					x_coord--;
				if (y_coord == 0)
					y_coord = Params.world_height-1;
				else
					y_coord++;
				break;
			case 4:				// West
				if (x_coord == 0)
					x_coord = Params.world_width-1;
				else
					x_coord--;
				break;
			case 5:				// Southwest
				if (x_coord == 0)
					x_coord = Params.world_width-1;
				else
					x_coord--;
				if (y_coord == Params.world_height-1)
					y_coord = 0;
				else
					y_coord--;
				break;
			case 6:				// South
				if (y_coord == Params.world_height-1)
					y_coord = 0;
				else
					y_coord--;
				break;
			case 7:				// SouthEast
				if (x_coord == Params.world_width-1)
					x_coord = 0;
				else
					x_coord++;
				if (y_coord == Params.world_height-1)
					y_coord = 0;
				else
					y_coord--;
				break;
		}
        this.energy -= Params.walk_energy_cost;
    }  // Accounts for going out of bounds

	protected final void run(int direction) {
		walk(direction);
		walk(direction);
		this.energy -= Params.run_energy_cost - (2*Params.walk_energy_cost);
    }  // Calls "walk" twice
	
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

	public void encounter() {
		for (int i = 0; i < CritterWorld.critterList.size(); i++) {
			Critter enemy = CritterWorld.critterList.get(i);
			if (this.x_coord == enemy.x_coord && this.y_coord == enemy.y_coord) {
				boolean Afight = true;
				boolean Bfight = true;
				if (!this.fight(enemy.toString())) {
					Afight = false;
					int walkDir = findAdjDir(x_coord, y_coord);
					if (walkDir != -1)
						this.walk(walkDir);
				}
				if (!enemy.fight(this.toString())) {
					Bfight = false;
					int walkDir = findAdjDir(enemy.x_coord, enemy.y_coord);
					if (walkDir != -1)
						enemy.walk(walkDir);
				}
				if (this.getEnergy() > 0 && enemy.getEnergy() > 0 && this.x_coord == enemy.x_coord && this.y_coord == enemy.y_coord) {
					if (Afight && Bfight) {
						if (Critter.getRandomInt(this.energy) >= Critter.getRandomInt(enemy.getEnergy())) {		// Critter A wins
							this.setEnergy((int)(this.getEnergy() + (.5*enemy.getEnergy())));
							enemy.setEnergy(0);
						} else {
							enemy.setEnergy((int)(enemy.getEnergy() + (.5*this.getEnergy())));
							this.setEnergy(0);
						}
					} else if (!Afight && Bfight) {
						if (Critter.getRandomInt(enemy.getEnergy()) > 0) {
							enemy.setEnergy((int)(enemy.getEnergy() + (.5*this.getEnergy())));
							this.setEnergy(0);
						} else {
							this.setEnergy((int)(this.getEnergy() + (.5*enemy.getEnergy())));
							enemy.setEnergy(0);
						}
					} else if (!Bfight && Afight) {
						if (Critter.getRandomInt(this.energy) > 0) {
							this.setEnergy((int)(this.getEnergy() + (.5*enemy.getEnergy())));
							enemy.setEnergy(0);
						} else {
							enemy.setEnergy((int)(enemy.getEnergy() + (.5*this.getEnergy())));
							this.setEnergy(0);
						}
					}
				}
			}
		}
	}

	private int findAdjDir(int x_OG, int y_OG) {
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

                //prepare critter for simulation, create initial position
                critter_instance.x_coord = getRandomInt(Params.world_width);    //set position with constrained randomizer
                critter_instance.y_coord = getRandomInt(Params.world_height);
//                critter_instance.oldCritPos = new Point(critter_instance.x_coord, critter_instance.y_coord);
//                pop.addLast(critter_instance);
                addToGrid(critter_instance);

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
		CritterWorld.algaeList.clear();
	}
	
	public static void worldTimeStep() {
		// Complete this method.
	}
	
	public static void displayWorld() {
		System.out.print("+");
		for (int i = 0; i < Params.world_width-1; i++) {
			System.out.print("-");
		}
		System.out.println("+");
		for (int i = 0; i < Params.world_height-1; i++) {
			System.out.print("|");
			for (int j = 0; j < Params.world_width-1; j++) {
				if (CritterWorld.findCritter(j, i)) {
					System.out.print(CritterWorld.findCritter(j,i));
				} else {
					System.out.print(" ");
				}
			}
			System.out.println("|");
		}
		System.out.print("+");
		for (int i = 0; i < Params.world_width-1; i++) {
			System.out.print("-");
		}
		System.out.println("+");
	}
	private static void addToGrid(Critter critter_instance){
	    Point critpos = new Point(critter_instance.x_coord, critter_instance.y_coord);
	    if(grid.containsKey(critpos)){
	        grid.get(critpos).add(critter_instance);
        }else{
	        LinkedList<Critter> crit = new LinkedList<Critter>();
	        crit.add(critter_instance);
	        grid.put(critpos,crit);
        }
    }
}
