# picross-game
A GUI JavaFX Program that allows the user to solve randomly generated Picross (Nonogram) puzzles.

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
    -Puzzles that come in 5x5, 10x10, and 15x15 sizes
    -A timer that keeps track of the time it takes to complete the puzzle
    -A togglable record of your solve times for that puzzle size, sorted by time
    -Randomly generated puzzles and a solution checker 
    -Mouse controlled interaction and active response graphics
    -Two different color themes

While the puzzles are randomly generated, I did not implement a checker that makes sure each
generated solution is uniquely solvable, as it wasn't a reasonable feature to include within
the time frame. As such, some puzzles may have multiple correct solutions; in that event, the
program will allow any such solution to be considered correct.

## Running the Game
The program is housed entirely within the classes built here. The following code was built and
run on Java 1.8 and uses features from JavaFX 8. Running *Main.java* will start the program.


