Test1: ParseMakeLargeCritter
File Input:make_large_critter
----------------------------
Functions Tested: make and show
Make 1000 Critters
Show them On the Board
Expected Output: Board Filled with Craig Critters


Test2: ParseMakeSmallCritter
File Input:make_small_critter
----------------------------
Functions Tested: make and show (make sure handles critter count appropriately)
Make 1 Critter
Show them on the Board
Expected Output: Board with 1 Critter


Test3: ParseInvalidInput
File Input: invalid_input
---------------------------
Functions Tested: invalid inputs
Expected Output: "Invalid inputs: [text here]"


Test4: KillCritters
File Input: kill_al_critters
---------------------------
Functions Tested: make, stats, and step
make 1000 Critters
Call stats to confirm 1000 Critters made
Step 100 times
Call stats to confirm 0 Critters remain
Expected Output: 1000 Critters before step. 0 Critters after step.


Test5: ParseSeed
File Input: seed_test
---------------------------
Functions Tested: make, seed, show
Use seed to force placement of Critter
Use show to confirm the Critter is in correct location
Expected Output: Critter in certain place in board


Test6: ParseErrors
File Input: error_processing
---------------------------
Functions Tested: make, seed, show, step
Expected Output: "Error Processing: [text here]"


Test7: InvalidCritter
File Input: invalid_make
---------------------------
Functions Tested: make
Expected Output: Error processing due to invalid class and improper Integer format


Test8: parseCraigStats
File Input: craig_stats
---------------------------
Functions Tested: make, stats
Creates Algae and Craig 
Calls stats
Expected Output: Expects proper number of Algae and Craigs to be made


Test9: parseStats
File Input: parse_stats
---------------------------
Functions Tested: make, stats
Creates Algae and Craig 
Calls Stats
Expected Output: Expects proper number of Algae and Craigs to be made
If Test8 Fails
Expects them to call stats for at least one critter other than Craig
