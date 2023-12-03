# Roborally
This is the Roborally project from Group A in [DTU Compute](https://compute.dtu.dk) course [02362-F20](https://kurser.dtu.dk/course/02362).

## Programmer's notes

### Branches
Please use the branches in the following way:
- `v0.3.1a`: This **WAS** previously the master branch. If you have any code that resides in here, please merge it into the `master` branch instead. 
- `master`: This is the main master branch. Please **only** commit / merge deployable code to this branch.
- `dev`: This is the main development branch. If you need to develop a specific feature and don't want to interfere with the other developers
please checkout this branch and merge back into it when you're done developing your awesome code :).

### Merge requests
Anyone can create new merge requests. Please use these as much as possible to ensure a consistency between new code.

## Working with the settings file
For the database connection to work you need to have your own `settings.json` file created in `main/resources/settings`. A sample has been provided,
which you can copy to the name `settings.json`, which is what the program is expecting. Note, that this file will not be added to Git.

## A note on the database
To avoid accidentally deleting the schema in your DBMS, the program will only create a database if you
don't have a database called the name set in your `settings.json` file. Therefore, if you wish to refresh your database, please drop the schema either using
MySQL commandline or a tool like MySQL Workbench.

## Running the program
1. Create the settings file as showed under "Working with the settings file"
2. Start the program in the class `RoborallyApplication`. Running this class's `main` method will start the program.
3. If you choose to start a new game, you can choose a board layout from your local drive. If you don't have one at hand, simply press cancel and a default one will be loaded for you.
Note that some classes have been renamed, so if you have boards from _previous_ versions there's **no guarantee** that the game will load. 

## Current features
1. Robots can push each other
2. Walls - robots can't walk through a wall
3. Gears - robots get turned if they're on a gear field at the end of a register
4. Checkpoints - the player has to pick up the checkpoints in the correct order to win the game
5. Conveyor belts - robots will get moved if they're on a conveyor belt at the end of a register
6. Pits - robots that are stationary on a pit field will be placed back to the start field.
7. Database attachment; You can save your game from "File" > "Save game".
8. Load game; You can load a previous game at the start of the program
9. Create board from different board layouts