package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Zack Campbell
 * zcc254
 * Audrey Gan
 * ayg333
 * Slip days used: <0>
 * Spring 2018
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import assignment4.Critter;

/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        
        // System.out.println("GLHF");
        ArrayList<String> input;                         //array list that holds the 2 initial input words
        input = parse(kb);
        while(input.size() != 0) {
            //put all code to run the critter world here
            input = parse(kb);
        }
        /* Write your code above */
        System.out.flush();

    }
    // Testing committing
    public static ArrayList<String> parse(Scanner keyboard) {
        ArrayList<String> input = new ArrayList<String>();
        String quit = "quit";
        String show = "show";
        String step = "step";
        String make = "make";
        String seed = "seed";
        String stats = "stats";
        String command = keyboard.next().toLowerCase();
        if(command.equals(quit)) {         //return empty array if user inputs "/quit"
            return input;   //input remains empty if user inputs quit
        }else{
            input.add(command);
        }
        if(command.equals(show)){
            Critter.displayWorld();
        }else if(command.equals(step)){
            Critter.worldTimeStep();
        }else if(command.equals(make)){
            String thisCritter = keyboard.next().toLowerCase();
            input.add(thisCritter);
            if(keyboard.hasNext()){
                int numCritters = keyboard.nextInt();
                String stringNum = Integer.toString(numCritters);
                input.add(stringNum);
                for(int i = 0; i<numCritters; i++){
                    try {
                        Critter.makeCritter(thisCritter);   //implements make with a count if present and valid critter class
                    } catch (InvalidCritterException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                try {
                    Critter.makeCritter(thisCritter);   //makes a single critter if it's a valid critter class
                } catch (InvalidCritterException e) {
                    e.printStackTrace();
                }
            }
        }else if(command.equals(seed)){
            int thisNum = keyboard.nextInt();
            String stringNum = Integer.toString(thisNum);
            input.add(stringNum);
            Critter.setSeed(thisNum);
        }else if(command.equals(stats)){
            String thisCritter = keyboard.next().toLowerCase();
            input.add(thisCritter);
            List<Critter> thisCritterList = new ArrayList();
            try {
                thisCritterList = Critter.getInstances(thisCritter);
            } catch (InvalidCritterException e) {
                e.printStackTrace();
            }
            Critter.runStats(thisCritterList);  //NOT SURE IF THIS SHIT IS RIGHT LOL
//            (Critter)thisCritter.runStats();
        }else{
            System.out.println("Invalid Command");
        }
        System.out.println("Input: " + input);
        return input;
    }
}
