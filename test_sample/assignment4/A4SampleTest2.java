package assignment4;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import assignment4.Critter.TestCritter;
import java.awt.Point;
import java.util.List;
public class A4SampleTest2 {

	private static final boolean DEBUG = false;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		Params.world_width = 20;
		Params.world_height = 20;
		Params.walk_energy_cost = 2;
		Params.run_energy_cost = 5;
		Params.rest_energy_cost = 1;
		Params.min_reproduce_energy = 20;
		Params.refresh_algae_count = 0;

		Params.photosynthesis_energy_amount = 1;
		Params.start_energy = 100;
		TestCritter.clearWorld();
	}

	@After
	public void tearDown() throws Exception {
	}

	// Utility method.
	/**
	 *  Move n steps in the specified direction.
	 *  @param current x, current y, direction to move, steps to move
	 *  @return new co-ordinates. 
	 */
	public static int[] moveStep(int x_coord, int y_coord, int direction, int n) {
		int w = Params.world_width; int h = Params.world_height;
		int newX = x_coord + w; int newY = y_coord + h;
		
		switch (direction) {
		case 0: newX = (newX += n); break;
		case 1: newX = (newX += n);
				newY = (newY -= n); break;
		case 2: newY = (newY -= n); break;
		case 3: newX = (newX -= n);
				newY = (newY -= n); break;
		case 4: newX = (newX -= n); break;
		case 5: newX = (newX -= n);
				newY = (newY += n); break;
		case 6: newY = (newY += n); break;
		case 7: newX = (newX += n); 
				newY = (newY += n); break;
		}
		return new int[]{newX%w, newY%h};
	}

	@Test
	/**
	 * 1.
	 * Use MakeCritter to create a Critter, and makes sure walk works for 1 step.
	 * @throws InvalidCritterException
	 */
	public void testWalk() throws InvalidCritterException {
		Critter.makeCritter("MyCritter1");
		MyCritter1 m1 = (MyCritter1) Critter.TestCritter.getPopulation().get(0);
		int x1a = m1.getX_coord(); int  y1a = m1.getY_coord();
		m1.doTimeStep();
		int x1b = m1.getX_coord(); int  y1b = m1.getY_coord();
		assertTrue((x1b - x1a == 1) || (x1b + Params.world_width - x1a) == 1);
		assertTrue(Math.abs(y1b - y1a) == 0);
	}	
	
	@Test
	/**
	 * 7.
	 * Walks 1 step each turn.  Check energy drop at each turn.
	 */
	public void WalkEnergyTest() throws InvalidCritterException {
		Critter.makeCritter("MyCritter1");
		MyCritter1 c = (MyCritter1) Critter.TestCritter.getPopulation().get(0);
		int step = 0;
		int energyUsePerStep = Params.rest_energy_cost + Params.walk_energy_cost;
		while (c.getEnergy() > 0) {
//		while (!c.isDead()) {
			assertEquals(Params.start_energy -step*energyUsePerStep, c.getEnergy());
			Critter.worldTimeStep();
			step++;
		}		
	}

	/**
	 * 11. Creates two Critters in the same spot, one being a runner. Checks to
	 * see if runner moved, lost energy, and lived
	 * 
	 * @throws InvalidCritterException
	 * 
	 */
	@Test
	public void RunDuringFightTest() throws InvalidCritterException {
		int x = 0;
		int y = 0;
		int num = 2;
		Critter.makeCritter("MyCritter6");
		MyCritter6 runner = (MyCritter6) Critter.getInstances("MyCritter6").get(0);
		Critter.makeCritter("MyCritter7");
		MyCritter7 fighter = (MyCritter7) Critter.getInstances("MyCritter7").get(0);
		runner.setX_coord(x);
		runner.setY_coord(y);
		fighter.setX_coord(x);
		fighter.setY_coord(y);

		assertEquals(num, TestCritter.getPopulation().size());
		Critter.worldTimeStep();
		if (DEBUG) {
			Critter.displayWorld();
		}
		assertFalse(runner.getEnergy() <= 0);
		assertEquals(Params.start_energy - Params.rest_energy_cost - Params.run_energy_cost, runner.getEnergy());
		assertTrue(runner.getX_coord() != x || runner.getY_coord() != y);
		assertTrue(fighter.getX_coord() == x && fighter.getY_coord() == y);
	}

}
