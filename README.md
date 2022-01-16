# Project: Connect Four (Connect_Four_game_Kotlin)
## About
Connect Four is a fun game for two players. The players place discs on a vertical board that is 7 columns wide and 6 rows tall. 
The goal of the game is to be the first to form a horizontal, vertical, or diagonal line of four discs. 
In this project, you will develop this game with a number of improvements.
## Stage 1/5: Input game parameters
### Description
Connect Four is a classic game where players drop colorful discs onto a vertical board. The main goal of the game is to form a line of four discs of the same color horizontally, vertically, or diagonally.

In this project, you will create a software version of this game. Typically, the game board has 6 rows and 7 columns, but in this version, the number of rows and columns can vary from 5 to 9.

In this stage, the program should read the players' names and the board size from the console input.

The board size is input in the following format: <Rows> X <Columns> or <Rows> x <Columns>, for example, 7 X 8 or 8x9. Whitespaces (spaces and tabs) have no effect.

### Objectives
The program should print the program title Connect Four, ask for the 1st player's name with the prompt First player's name:, and read it. Then it should ask for the 2nd player's name with the prompt Second player's name: and read it.

Subsequently, the program should ask for the board dimensions with the following prompt:

Set the board dimensions (Rows x Columns)
Press Enter for default (6 x 7)
Once the dimensions are entered, it should read them. Ignore any whitespaces. If users press the Enter button right away (ignoring the dimension prompt), then the board size is 6 rows and 7 columns.

If the number of rows is outside the 5-9 range, print the following message Board rows should be from 5 to 9 and ask for dimensions once again.

If the number of columns is outside the 5-9 range, print the following message Board columns should be from 5 to 9 and ask for dimensions once again.

If users fail to input dimensions in the correct format, print Invalid input and ask for dimensions once again.

Finally, output the following message:

<1st player's name> VS <2nd players name>
<Rows> X <Columns> board

## Stage 2/5: Game board
### Description
In this stage, you need to draw the game board by using the ║, ╚, ═, ╩, ╝ box-drawing characters. You can find more information about these characters in the corresponding Wikipedia article, but you can also simply copy them.

Print the column number above each respective column and use the above-mentioned box-drawing characters for creating the board lines.

There is the possibility, due to a known issue, that the ║, ╚, ═, ╩, ╝ box-drawing characters can't be correctly printed at the console output. Instead the ? character is printed for each of them. If this situation arises, then implement the board by using the plain text | and = plain text characters as following. Either case will be accepted as a valid solution.

### Objectives
1. Draw and print the board set by users according to the procedure above;
2. Print the column numbers on the board. Mind the spaces between characters.

## Stage 3/5: Game logic
### Description
Players use red and yellow colored discs in the "hardware" version of the game. In this project, we are going to substitute them with o and * characters. The o is for the first player.

Each player inputs a column number one after another. The program must read the input and print the appropriate character ( o or *) on the first available position at the bottom of that column. If the user input isn't correct, print the appropriate message and ask for a new one. The program should also check whether a column is full or not. If it is, no more discs can be added to it.

### Objectives
In addition to the functionality from the previous stage, your game should perform the following:

1. Ask each player to input a column number by prompting <First player's name>'s turn: or <Second player's name>'s turn:. Read the column number and print o or * on the first available position of that column. The first player is o; the second player is *. If either player inputs end, terminate the program and print Game over!.
2. If the input contains an integer number outside the scope of available columns, warn the players with the The column number is out of range (1 - <Max column number>) message and ask for it once again. If players' input doesn't contain an integer, warn the players with Incorrect column number and ask for it once again.
3. If the column is full, print the following message Column <Column number> is full and ask to input another column number.

If players input end instead of a column number, terminate the program and print the respective message.
## Stage 4/5: Winning condition
### Description
Now, it is time to implement the winning condition. A player wins when they place four discs of the same color in a row horizontally, vertically, or diagonally. After each move, the program checks if the condition is met. Also, if the board is full and the win condition isn't fulfilled, claim it a draw.

### Objectives
In addition to what we have added before, your program should do the following:

1. Check the board for the winning condition. If a player wins, output Player <Player's name> won;
2. If the board is full, but neither of the players has won, print It is a draw;
3. Regardless of whether it is a draw or somebody's victory, print Game Over! and terminate the program.

## Stage 5/5: Multiple games
### Description
Before this stage, two players can play only one game. We are going to change this. In this stage, implement the option to play multiple games.

After setting the board dimensions, ask the players if they would like to play a single or multiple games. In the latter case, keep and print the score. When a player wins a game, they get 2 points. If it's a draw, give 1 point to each player.

If the players have chosen the multiple game option, alternate the first move for each new game. However, each player retains the same disc symbol.

### Objectives
After setting the board dimensions, players see the following:

Do you want to play single or multiple games?
For a single game, input 1 or press Enter
Input a number of games:
Only positive digits are valid as for input (except 0). If a player inputs anything else, print Invalid input and ask for another try.

If players input 1 or press Enter, start a single game and print the following message:

<1st player's name> VS <2nd player's name>
<Rows> X <Columns>
Single game
In this case, the gameplay remains the same.

If players input an integer that is bigger than 1, start the multiple game mode and print the following message:

<1st player's name> VS <2nd player's name>
<Rows> X <Columns>
Total <Number of games> games
Print the score after each finished game in the following format:

Score
<1st player's name>: 2 <2nd player's name>: 2
Before the start of a game, print the game number: Game #<Number of game>. Players take turns for the first move, but each player keeps the same disc symbol throughout all games.

At any point of the game, if players input end, your program should output Game over! and terminate the game.
