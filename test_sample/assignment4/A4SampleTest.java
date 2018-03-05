package assignment4;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import org.junit.Test;

import assignment4.Critter.TestCritter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import static org.hamcrest.CoreMatchers.containsString;

public class A4SampleTest{

	private static String TESTSRCDIR = "test_sample/";
	private static  ByteArrayOutputStream outContent;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		TestCritter.clearWorld();
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void tearDown() throws Exception {
	}

	
	/*
	 * 4. KillCritters
	 * Test: killCritters
     * Test for make critter and stats, and step
     * Creates large number of make critters and compare stats after 500 steps
     * Expects all Critters to be dead
     */
	 
	@Test 
	public void KillCritters(){
	
		
		//Uncomment Following Codeblock to test 
		//Remove final keyword from Params.java

		Params.world_width = 20;
		Params.world_width = 20;
		Params.walk_energy_cost = 2;
		Params.run_energy_cost = 5;
		Params.rest_energy_cost = 1;
		Params.min_reproduce_energy = 20;
		Params.refresh_algae_count = (int)Math.max(1, Params.world_width*Params.world_height/1000);
		Params.photosynthesis_energy_amount = 1;
		Params.start_energy = 5;
		
		String fileFolder = "kill_all_critter";
		String[] inputs = {TESTSRCDIR + fileFolder + "/input.txt" ,"test"};
		
		Main.main(inputs);
		outContent = Main.testOutputString;
		
		Scanner scanner = null;
		try {
			scanner = new Scanner( new File(TESTSRCDIR + fileFolder + "/expected_output.txt") );
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}
		String text = scanner.useDelimiter("\\A").next().trim();
		String output =cleanString(outContent.toString());
		scanner.close();
		
		assertEquals(text,output);
	}
	
	
	
	
	/*
	 * 6. ParseErrors
	 * 	
     * Test: ParseErrors
     * Test for errors within valid inputs
     * Expects errors to be printed
	 */
	
	@Test 
	public void ParseErrors(){
		
	
		//Uncomment following codeblock to test with parameters
		//Remove final keyword in Params.java
				
		Params.world_width = 20;
		Params.world_width = 20;
		Params.walk_energy_cost = 2;
		Params.run_energy_cost = 5;
		Params.rest_energy_cost = 1;
		Params.min_reproduce_energy = 20;
		Params.refresh_algae_count = (int)Math.max(1, Params.world_width*Params.world_height/1000);
		Params.photosynthesis_energy_amount = 1;
		Params.start_energy = 100;
		
		
		
		String fileFolder = "error_processing";
		String[] inputs = {TESTSRCDIR + fileFolder + "/input.txt" ,"test"};
		
		Main.main(inputs);
		outContent = Main.testOutputString;
		
		Scanner scanner = null;
		try {
			scanner = new Scanner( new File(TESTSRCDIR + fileFolder + "/expected_output.txt") );
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}
		String text = scanner.useDelimiter("\\A").next().trim();
		String output = cleanString(outContent.toString());
		scanner.close();
		assertThat(text, containsString(output));
		
	}
	
	/*
	 * 10. showEmptyWorld
	 */
	
	@Test
	public void showEmptyWorld(){
		
		/*
		 * Test: InvalidCritter
		 * Expects error in creating critters
		 */
		Params.world_width = 20;
		Params.world_width = 20;
		Params.walk_energy_cost = 2;
		Params.run_energy_cost = 5;
		Params.rest_energy_cost = 1;
		Params.min_reproduce_energy = 20;
		Params.refresh_algae_count = (int)Math.max(1, Params.world_width*Params.world_height/1000);
		Params.photosynthesis_energy_amount = 1;
		Params.start_energy = 100;
		
		String fileFolder = "empty_world";
		String[] inputs = {TESTSRCDIR + fileFolder + "/input.txt" ,"test"};
		
		
		Main.main(inputs);
		outContent = Main.testOutputString;
		
		
		
		Scanner scanner = null;
		try {
			scanner = new Scanner( new File(TESTSRCDIR + fileFolder + "/expected_output.txt") );
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}
		
		String text = scanner.useDelimiter("\\A").next().trim();
		String output = cleanString(outContent.toString());
		scanner.close();
		assertEquals(text,output);
		
		
	}
	

	/*
	 * 1. ParseMakeLargeCritter
	 */
	
	@Test 
	public void ParseMakeLargeCritter(){
		// Test for make and show command
		// Expects entire board to be filled with critters
		
		
		Params.world_width = 20;
		Params.world_width = 20;
		Params.walk_energy_cost = 2;
		Params.run_energy_cost = 5;
		Params.rest_energy_cost = 1;
		Params.min_reproduce_energy = 20;
		Params.refresh_algae_count = (int)Math.max(1, Params.world_width*Params.world_height/1000);
		Params.photosynthesis_energy_amount = 1;
		Params.start_energy = 100;
		
		String fileFolder = "make_large_critter";
		String[] inputs = {TESTSRCDIR + fileFolder + "/input.txt" ,"test"};
		
		Params.world_width = 20;
		Params.world_height = 20;
	
		Main.main(inputs);
		outContent = Main.testOutputString;
		
		Scanner scanner = null;
		try {
			scanner = new Scanner( new File(TESTSRCDIR + fileFolder +"/expected_output.txt") );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String text = scanner.useDelimiter("\\A").next().trim();
		String output = cleanString(outContent.toString());
		scanner.close();
		assertEquals(text, output);
	}
	
	String cleanString(String input)
	{
		input = input.replace("critter>", "")
					 .replace("critters>", "")
			         .replace("\r\n", "\n")
			         .trim();
		
		return input;
	}
	
	
}
