# Gomoku Plan

## App
* Creates a new ui.GomokuController
* calls run() on the controller

## Set Up
### ui.GomokuController
* displays a welcome message
* calls displayPlayerMenu(String player) to display the menu for player selection
* takes the name either from human input or random cpu and calls setUpPlayer(String name) to create a new player
* repeat for player 2
* create game.Gomoku instance

## Game Loop

### Controller
* call printBoard() which calls getStones() on the Gomoku instance. It iterates through the board and prints stones from the list.
### game.Gomoku
* get the current player from call getCurrent()
### Controller
* call makeMove(Player currentPlayer) which loops.
* if cpu player, then call RandomPlayer.generateMove()
* if human player, get input for row and column
* check the move by calling Gomoku.place(Stone stone)
* if it's a valid move, great, repeat the game loop
* if it's a game over or draw, ask if you want to play again.
