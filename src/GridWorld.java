package gridWorld;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;

public class GridWorld {
	    static final int GRID_SIZE = 50;
	    static final int INITIAL_HEALTH = 200;
	    static final int INITIAL_MOVES = 450;

	    
	    static int blankProbability = 0;
	    static int speederProbability = 0;
	    static int lavaProbability = 0;
	    static int mudProbability = 0;

	    static int blankHealthLoss = 0;
	    static int speederHealthLoss = -5;
	    static int lavaHealthLoss = -50;
	    static int mudHealthLoss = -10;

	    static int leastHealthLost = -200;
	    static int skipped = 0;
	    
	    static String bestPath="";
	    
	    static GridState[][] grid;
	    
	    enum GridState {
	        BLANK, SPEEDER, LAVA, MUD
	    }

	    public static void main(String[] args) {
	        configureStateProbability();
	        grid = generateGrid();
	        int m = GRID_SIZE; // Number of rows
	        int n = GRID_SIZE; // Number of columns
	        LocalDateTime currentTime = LocalDateTime.now();
	        findPaths(0, 0, m, n, "");

	        System.out.println("Generated Grid: ");
	        displayGrid(grid);

	        System.out.println();
	        System.out.println("BestPath : "+ bestPath);
	        System.out.println();
	        int health = INITIAL_HEALTH;
	        int moves = INITIAL_MOVES;
	        int pathIndex=0;
	        int x = 0, y = 0; // Starting point (top-left corner)
	        int targetX = GRID_SIZE - 1, targetY = GRID_SIZE - 1; // Ending point (bottom-right corner)
	        // Check the outcome
	        if (bestPath.length()>0) {
	            System.out.println("Most efficient route found");
		        System.out.println("Moves made with remaining health and moves left");
		        while ((x != targetX || y != targetY) && health > 0 && moves > 0) {
		        	if (bestPath.charAt(pathIndex)=='Y') {
		        		y++;
		        	}
		        	else {
		        		x++;
		        	}
		        	pathIndex++;
		            // Update health and moves based on the grid state
		            GridState state = grid[x][y];
		            switch (state) {
		                case BLANK:
		                	moves -= 1;
		                    break;
		                case SPEEDER:
		                    health -= 5;
		                    break;
		                case LAVA:
		                    health -= 50;
		                	moves -= 10;
		                    break;
		                case MUD:
		                    health -= 10;
		                	moves -= 5;
		                    break;
		            }
	
		            // Print current status
		            System.out.println("Position: (" + x + ", " + y + "), Health: " + health + ", Moves left: " + moves);
		        }
	        } else {
	            System.out.println("Grid World Level is not solvable");
	        }
	    }

	    // Have user set probabilities of each state in grid
	    private static void configureStateProbability() {
	    	boolean notConfigured=true;
	    	int totalProbability=0;
	        System.out.println("Configure Probability of each of the states.  (Must add up to 100)");
	        System.out.println("There are 4 states.  Below is the health affect of each.");
	        System.out.println("Blank  Health 0  Moves -1");
	        System.out.println("Speeder  Health -5  Moves 0");
	        System.out.println("Lava  Health -50  Moves -10");
	        System.out.println("Mud  Health -10  Moves -5");
	        System.out.println();
	        try {
	        	Scanner s = new Scanner(System.in);
	        	while (notConfigured) {
			        System.out.println("Enter Probability for Blank");
			        blankProbability=s.nextInt();
			        System.out.println("Enter Probability for Speeder");
			        speederProbability=s.nextInt();
			        System.out.println("Enter Probability for Lava");
			        lavaProbability=s.nextInt();
			        System.out.println("Enter Probability for Mud");
			        mudProbability=s.nextInt();
			        totalProbability = blankProbability+speederProbability+lavaProbability+mudProbability;
			        if (totalProbability==100) {
			        	speederProbability+=blankProbability;
			        	lavaProbability+=speederProbability;
			        	mudProbability+=lavaProbability;
			        	notConfigured=false;
			        }
			        else {
				        System.out.println("Total Probability must add up to 100.  Please re-enter");			        	
			        }
	        	}
		        s.close();
		        
	        }
	        catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }
	    
	    // Recursive function to find best path
	    private static void findPaths(int row, int col, int m, int n, String path) {
        	int healthLoss=0;
        	// Have reached endpoint of grid
	        if (row == m - 1 && col == n - 1) {
	        	healthLoss = calculateHealthLoss(path);
	        	if (healthLoss>leastHealthLost) {
	        		leastHealthLost = healthLoss;
	        		bestPath = path;
	        	}
	            return;
	        }

   	        // Move down if within bounds
       		if (row < m - 1) {
               	healthLoss = calculateHealthLoss(path);
       	        // Only continue on path if still better health than best found so far.
               	if (healthLoss>leastHealthLost) {
	        		findPaths(row + 1, col, m, n, path + "Y");
	        	}
       		}
    	    // Move right if within bounds
    	    if (col < n - 1) {
               	healthLoss = calculateHealthLoss(path);
       	        // Only continue on path if still better health than best found so far.
               	if (healthLoss>leastHealthLost) {
   	        		findPaths(row, col + 1, m, n, path + "X");
    	        }
        	}
        	else {
        		skipped++;
	        }
	    }
	
	    // Calculate the health loss for a given path
	    private static int calculateHealthLoss(String path) {
	    	int healthLoss=0;
	    	int x=0;
	    	int y=0;
	    	for (int i=0;i<path.length();i++) {
	    		if (path.charAt(i)=='Y') {
	    			y++;
	    		}
	    		else {
	    			x++;
	    		}
	    		if (grid[x][y].equals(GridState.SPEEDER)){
	    			healthLoss+=speederHealthLoss;
	    		}
	    		else if (grid[x][y].equals(GridState.LAVA)){
	    			healthLoss+=lavaHealthLoss;
	    		}
	    		else if (grid[x][y].equals(GridState.MUD)){
	    			healthLoss+=mudHealthLoss;
	    		}
 	    	}
	    	
	    	return healthLoss;
	    }

	    // Generate a random grid with BLANK, SPEEDER, LAVa, and MUD states
	    private static GridState[][] generateGrid() {
	        GridState[][] grid = new GridState[GRID_SIZE][GRID_SIZE];
	        Random random = new Random();

	        for (int i = 0; i < GRID_SIZE; i++) {
	            for (int j = 0; j < GRID_SIZE; j++) {
	                int rand = random.nextInt(100);
	                if (rand < blankProbability) {
	                    grid[i][j] = GridState.BLANK;
	                } else if (rand < speederProbability) {
	                    grid[i][j] = GridState.SPEEDER;
	                } else if (rand < lavaProbability) {
	                    grid[i][j] = GridState.LAVA;
	                } else {
	                    grid[i][j] = GridState.MUD;
	                }
	            }
	        }

	        return grid;
	    }
	    
	    // Display the generated grid
	    private static void displayGrid(GridState[][] grid) {
	        for (int i = 0; i < GRID_SIZE; i++) {
	            for (int j = 0; j < GRID_SIZE; j++) {
	    	        System.out.print(grid[i][j]+" ");
	            }
	            System.out.println();
            }
	    }
	}
