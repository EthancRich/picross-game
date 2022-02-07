# Picross-Game
A GUI JavaFX Program that allows the user to solve randomly generated Picross (Nonogram) puzzles.

![This is an Example 5x5 Puzzle](/Images/Picross_Sample_1.PNG)
![This is an Example 10x10 Puzzle](/Images/Picross_Sample_2.PNG)

## Motivation
For my CSE 205 Honors Project, I wanted to take another stab at a Nonogram program. Previously
I attempted a program that would solve Nonograms, but that soon went over my head and blew up
in my face a bit. Taking CSE 205 gave me the opportunity to work with GUI for the first time,
and it gave me the idea to revive the Nonogram idea, but this time to just make the game portion.

## Build Status: Complete
The code was built over the course of 2 months. I didn't know how to use Git and GitHub at the
time, so this project is being posted after learning how to use these systems. Therefore, this
is the final build state, unless I come back to it to improve it.

## Features
This project is a full-fledged Picross game, featuring:
- Puzzles that come in 5x5, 10x10, and 15x15 sizes
- A timer that keeps track of the time it takes to complete the puzzle
- A togglable record of your solve times for that puzzle size, sorted by time
- Randomly generated puzzles and a solution checker 
- Mouse controlled interaction and active response graphics
- Two different color themes

While the puzzles are randomly generated, I did not implement a checker that makes sure each
generated solution is uniquely solvable, as it wasn't a reasonable feature to include within
the time frame. As such, some puzzles may have multiple correct solutions; in that event, the
program will allow any such solution to be considered correct.

## Running the Game
The program is housed entirely within the classes built here. The following code was built and
run on Java 1.8 and uses features from JavaFX 8. Running *Main.java* will start the program.

## UML Diagram and Program Structure
Below is the layout for the classes and their relationships.
![First Part of the UML Diagram](/Images/UML_Diagram_1.png)
![Second Part of the UML Diagram](/Images/UML_Diagram_2.png)
These were split into two prior, as I needed to fit them to a slide show, but it'll suffice.
Most of the structure can be inferred by the colors of the classes in the Diagram.
- Blue classes are the main Panes housed in the center of the Root BorderPane. These are swapped as buttons navigate between them.
- Yellow classes are button handlers that operate on certain button presses.
- Green classes are the panes that handle the contents of the game. There is some redundancy in the panes that make up the row and column hints, but ultimately I found it was easier to separate them into two different classes at the time.
- White classes are purely information classes, storing the data of the state of the puzzle and the individual cells.
- Purple class cannot be instantiated, and is only referenced to get the current state of the selected color themes.

## How to Play
If you are unfamiliar with how to solve Picross/Nonogram puzzles, you can read how to play [here](https://www.hanjie-star.com/en-us/how-to-solve-picross/solve-first-picross-puzzle).

### Controls
- Mouse 1 is **Fill**. Filling is the main criteria for solving the puzzle.
- Mouse 2 is **Cross**. Crossing helps determine which cells must be filled.
- Mouse 3 is **Circle**. Circling is used to quickly visually count cells, as well as test possible fill spots for cells.

Clicking and dragging creates multiple of that shape over wherever your mouse goes. Fills and Crosses have higher *priority* than Circles, meaning dragging to create or erase circles will not impact that any pre-existing Fills or Crosses. Dragging with Fills or Crosses can override other Fills or Crosses.

You may also click the hint numbers in the rows and columns. For larger puzzles, it helps to keep track of what hints have been completed and what hints need to still be figured out.
