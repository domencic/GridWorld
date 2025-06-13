#GridWorld

This program completes assignment 3 in the Take Home exercise.  It takes user input of what the probabilites they wish to have for each of the grid states.

The Grid States are as follows:
  “Blank”: {“Health”: 0, “Moves”: -1},
  “Speeder”: {“Health”: -5, “Moves”: 0},
  “Lava”: {“Health”: -50, “Moves”: -10},
  “Mud”: {“Health”: -10, “Moves”: -5}

After the user has entered the probabilities for each of the states, the program generates the grid.

Then the program finds the optimal path if one exists
It then prints out the generated grid and the optimal path with how much health and moves remain.

