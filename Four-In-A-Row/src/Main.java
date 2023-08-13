import java.util.*;

public class Main {
    public static void main(String[] args) {
        initBoard();
        newGame();
        drawBoard();
        while (!gameOver) {
            beginRound();
        }
    }

    static int[][] board;
    static String player1;
    static String player2;

    static Boolean player1first;
    static Boolean gameOver = false;
    static boolean bot1 = false;
    static boolean bot2 = false;
    static boolean debugMode = false;

    static List<int[]> possibles;


    public static void initBoard() {
        board = new int[6][7];
    }

    public static void drawBoard() {
        System.out.println();
        System.out.println("1 2 3 4 5 6 7 ");
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 6; j++) {
                if (board[i][j] == 0) {
                    System.out.print("_ ");
                } else if (board[i][j] == 1) {
                    System.out.print("X ");
                } else {
                    System.out.print("0 ");
                }
                if (j == 6) {
                    System.out.print("\n");
                }
            }
        }
        System.out.println();
    }

    public static void newGame() {
        Scanner myScanner = new Scanner(System.in);
        System.out.print("Player #1, enter your name: ");
        player1 = myScanner.nextLine();
        System.out.println("Hello, " + player1 + ".\n");

        String input = "";
        while (!input.equals("1") || !input.equals("2") || !input.equals("3")) {
            System.out.println("For player 2: \npress 1 for human \npress 2 for simple random bot \npress 3 for an intelligent bot : ");
            input = myScanner.nextLine();

            if (input.equals("1")) {
                System.out.print("Player #2, enter your name: ");
                player2 = myScanner.nextLine();
                System.out.println("Hello, " + player2 + ".");
                bot1 = false;
                bot2 = false;
                break;

            } else if (input.equals("2")) {
                player2 = "simple random Bot";
                System.out.println("Hello, " + player2 + ".");
                bot1 = true;
                bot2 = false;
                break;
            } else if (input.equals("3")) {
                player2 = "intelligent Bot";
                System.out.println("Hello, " + player2 + ".");
                bot1 = false;
                bot2 = true;
                break;
            } else {
                System.out.println("Please try again");
            }
        }

        System.out.println();
        System.out.println("(Randomizing...)");
        if (Math.random() > 0.5) {
            player1first = true;
            System.out.println("It's " + player1 + "'s turn.");
        } else {
            player1first = false;
            System.out.println("It's " + player2 + "'s turn.");

        }
    }

    public static void beginRound() {
        if (player1first) {
            playerTurn(player1, 1);
        } else {
            playerTurn(player2, 2);
        }
    }

    public static void playerTurn(String player, int id) {
        String message = "";
        String input = "";
        int output = 0;

        if (id == 2 && bot2) {
            System.out.print(player + ", choose a column: ");
            output = intBot();
            System.out.println(output + 1);

        } else if (id == 2 && bot1) {
            while (true) {
                System.out.print(player + ", choose a column: ");
                output = (int) (Math.random() * 7);
                System.out.println(output + 1);
                if (board[0][output] != 0) {
                    message = ("Full. Please input a valid column number that is not full");
                    System.out.println(message);
                    continue;
                } else {
                    break;
                }
            }
        } else {

            while (input.isBlank()) {
                System.out.print(player + ", choose a column: ");

                Scanner myScanner = new Scanner(System.in);
                input = myScanner.nextLine();

                if (input.isBlank()) {
                    continue;
                }

                if (input.equals("q")) {
                    System.out.println("Thanks for playing! Goodbye");
                    System.exit(0);
                }
                //debugs and tests
                if (input.charAt(0) == 'W') {
                    char idChar = input.charAt(1);
                    int checkId = Integer.parseInt(String.valueOf(idChar));
                    char lineSize = input.charAt(2);
                    int checkLineSize = Integer.parseInt(String.valueOf(lineSize));
                    findMove(checkId, checkLineSize, false, true);
                }
                if (input.charAt(0) == 'D') {
                    debugMode = !debugMode;
                    System.out.println("Debug Mode is set to : " + debugMode);
                }
                try {
                    output = Integer.parseInt(input) - 1;
                } catch (NumberFormatException nfe) {
                    message = ("Not and integer. Please input a valid column number integer");
                    System.out.println(message);
                    input = "";
                    continue;
                }
                if (board[0][output] != 0) {
                    message = ("Full. Please input a valid column number that is not full");
                    System.out.println(message);
                    input = "";
                    continue;
                }
                break;


            }
        }
        //call a method to drop a disc
        dropDisc(output, id);

        //change to the next player
        player1first = !player1first;

    }

    public static void dropDisc(int col, int id) {
        int cRow = 0;
        int cCol = 0;
        for (int i = 5; i >= 0; i--) {
            if (board[i][col] == 0) {
                board[i][col] = id;
                cRow = i;
                cCol = col;
                break;
            }
        }
        drawBoard();
        checkWinner(cCol, cRow, id);
    }

    public static void checkWinner(int col, int row, int id) {
        //see if the new line has a length of 4 or more
        ArrayList<DoublyLinkedList> chipLines = new ArrayList<DoublyLinkedList>();
        for (int direction = 0; direction < 4; direction++) { //there are four possible directions atm, so go through 4 iterations
            chipLines.add(findLine(row, col, id, 4, direction, false, false));
        }

        //check the array list
        //if there is a line, it's a winning line
        for (DoublyLinkedList line : chipLines) {
            if (line != null) {
                winner(id);
            }
        }

        //check it it's a draw
        //update the possible moves list
        possibleMoves();
        if (possibles.isEmpty()) {
            callDraw();
        }


    }

    public static void winner(int id) {
        if (id == 1) {
            System.out.println(player1 + " wins!");
        } else {
            System.out.println(player2 + " wins!");
        }
        playAgain();
    }

    public static void callDraw() {
        System.out.println("It's a draw!");
        playAgain();
    }

    public static void playAgain() {
        String input = "";
        String message = "";
        while (true) {
            System.out.println("Play again? [y/n]");
            Scanner myScanner = new Scanner(System.in);
            input = myScanner.nextLine();
            input = input.toLowerCase();

            if (input.equals("y") || input.equals("n")) {
                if (input.equals("y")) {
                    initBoard();
                    newGame();
                    drawBoard();
                } else {
                    System.out.println("Thanks for playing! Goodbye");
                    System.exit(0);
                }
            } else {
                message = ("Please input 'y' or 'n'");
                System.out.println(message);
                continue;
            }
            break;
        }
    }

    public static void possibleMoves() {
        possibles = new ArrayList<int[]>();
        for (int i = 0; i <= 5; i++) { //check rows
            for (int j = 0; j <= 6; j++) { //check cols
                if (board[i][j] == 0) {
                    if (i == 5) { //at the bottom row, add to the possible spots
                        possibles.add(new int[]{i, j});
                    } else if (board[i + 1][j] != 0) { //there's a chip in the next row down of this column
                        possibles.add(new int[]{i, j});
                    }
                }
            }
        }

    }

    public static DoublyLinkedList findLine(int row, int col, int id, int countMax, int direction, boolean print, boolean checkingConnector) {

        //create a list
        //put in the first chip based on the parameters
        //put the count to 1
        DoublyLinkedList line = new DoublyLinkedList();
        line.insertFront(new int[]{row, col, id, 0});
        int count = 1;

        //get the specifications of the direction to change the for loop parameters
        int frontInit = 0;
        boolean frontCondition = true;
        int frontIterator = 0;
        int backInit = 0;
        boolean backCondition = true;
        int backIterator = 0;
        int fRow = row;
        int fCol = col;
        int bRow = row;
        int bCol = col;
        String code = "";

        switch (direction) {
            case 0: //left-right
                frontInit = col - 1;
                frontCondition = frontInit >= 0;
                frontIterator = -1;
                fCol = frontInit;

                backInit = col + 1;
                backCondition = backInit <= 6;
                backIterator = 1;
                bCol = backInit;
                code = "left-right";
                break;
            case 1: //up-down
                frontInit = row - 1;
                frontCondition = frontInit >= 0;
                frontIterator = -1;
                fRow = frontInit;

                backInit = row + 1;
                backCondition = backInit <= 5;
                backIterator = 1;
                bRow = backInit;
                code = "up-down";
                break;
            case 2: //up-left
                frontInit = 1;
                frontCondition = (row - frontInit >= 0 && col - frontInit >= 0);
                frontIterator = 1;
                fRow = row - frontInit;
                fCol = col - frontInit;

                backInit = 1;
                backCondition = row + backInit <= 5 && col + backInit <= 6;
                backIterator = 1;
                bRow = row + backInit;
                bCol = col + backInit;
                code = "up-left";
                break;
            case 3: //up-right
                frontInit = 1;
                frontCondition = row - frontInit >= 0 && col + frontInit <= 6;
                frontIterator = 1;
                fRow = row - frontInit;
                fCol = col + frontInit;

                backInit = 1;
                backCondition = row + backInit <= 5 && col - backInit >= 0;
                backIterator = 1;
                bRow = row + backInit;
                bCol = col - backInit;
                code = "up-right";
                break;
        }


        for (; frontCondition; frontInit += frontIterator) { //front links
            //update the info
            switch (direction) {
                case 0: //left-right
                    frontCondition = frontInit > 0;
                    fCol = frontInit;
                    break;
                case 1: //up-down
                    frontCondition = frontInit > 0;
                    fRow = frontInit;
                    break;
                case 2: //up-left
                    frontCondition = row - frontInit > 0 && col - frontInit > 0;
                    fRow = row - frontInit;
                    fCol = col - frontInit;
                    break;
                case 3: //up-right
                    frontCondition = row - frontInit > 0 && col + frontInit < 6;
                    fRow = row - frontInit;
                    fCol = col + frontInit;
                    break;
            }

            if (board[fRow][fCol] == id) { //there's a chip, add it to the doulbylinked list
                //if we aren't checking the connection, increase the count and add the chip
                int[] chipInfo = {fRow, fCol, id, 0};
                line.insertFront(chipInfo);
                count++;

            } else if (board[fRow][fCol] == 0) {//there's no chip, the line ends.
                //add the blank chip
                int[] chipInfo = {fRow, fCol, 0, 0};
                line.insertFront(chipInfo); //put the blank space on the list for later
                break;

            } else {
                break;
            }
        }

        for (; backCondition; backInit += backIterator) { //back links
            //update the info
            switch (direction) {
                case 0: //left-right
                    backCondition = backInit < 6;
                    bCol = backInit;
                    break;
                case 1: //up-down
                    backCondition = backInit < 5;
                    bRow = backInit;
                    break;
                case 2: //up-left
                    backCondition = row + backInit < 5 && col + backInit < 6;
                    bRow = row + backInit;
                    bCol = col + backInit;
                    break;
                case 3: //up-right
                    backCondition = row + backInit < 5 && col - backInit > 0;
                    bRow = row + backInit;
                    bCol = col - backInit;
                    break;
            }

            if (board[bRow][bCol] == id) {
                //if we aren't checking the connection, increase the count and add the chip
                int[] chipInfo = {bRow, bCol, id, 0};
                line.insertBack(chipInfo);
                count++;

            } else if (board[bRow][bCol] == 0) {
                int[] chipInfo = {bRow, bCol, 0, 0};
                line.insertBack(chipInfo);
                //break if not checking the connection
                break;
            } else {
                //there's nothing there, break
                break;
            }
        }

        if (print) {
            System.out.println("Count: " + count + ", count max: " + countMax);
            System.out.println("Printing all lines " + code + " from chip at: " + row + ", " + col + ", id:" + id);
            line.printList();
            if (count >= countMax) {
                System.out.println("Count " + code + " is within range of max ");

            }
            System.out.println();

        }
        //check if there is a connector when count = 2 and //&& countMax == 2 countMax == 2
        if (count == 2) {
            //the head or tail must be blank in order to be a connector
            if (line.head.info[2] == 0) {
                //check the line from that point using the same direction
                ArrayList<DoublyLinkedList> connectors = new ArrayList<DoublyLinkedList>();
                //check first, then make sure you are checking so it doesn't loop
                if (!checkingConnector) {
                    connectors.add(findLine(line.head.info[0], line.head.info[1], id, 2, direction, print, true));
                }
                for (DoublyLinkedList conLine : connectors) {
                    if (conLine != null) {
                        //the head is a connector
                        if (print) System.out.println("The head is a connector");
                        line.head.info[3] = 1;
                    }
                }
            }
            if (line.tail.info[2] == 0) {
                //check the line from that point using the same direction
                ArrayList<DoublyLinkedList> connectors = new ArrayList<DoublyLinkedList>();
                if (!checkingConnector) {
                    connectors.add(findLine(line.tail.info[0], line.tail.info[1], id, 2, direction, print, true));
                }
                for (DoublyLinkedList conLine : connectors) {
                    if (conLine != null) {
                        //the head is a connector
                        if (print) System.out.println("The tail is a connector");
                        line.tail.info[3] = 1;
                    }
                }
            }
        }

        if (count >= countMax) {
            return line;
        }

        return null;


    }

    public static void iterateBoard(int id, int countMax, ArrayList<DoublyLinkedList> chipLines, boolean print) {
        for (int row = 0; row <= 5; row++) { //check rows
            for (int col = 0; col <= 6; col++) { //check cols
                if (board[row][col] == id) {
                    for (int direction = 0; direction < 4; direction++) { //there are four possible directions atm, so go through 4 iterations
                        chipLines.add(findLine(row, col, id, countMax, direction, print, false));
                    }
                }
            }
        }

    }

    /**
     * finds possible column choices based on lines according to the parameters
     *
     * @param id       the chip id that you are looking at lines for
     * @param lineSize the length of the lines you are looking for
     * @param print    prints debugging information
     */
    public static ArrayList<Integer> findMove(int id, int lineSize, boolean twoEmpties, boolean print) {

        ArrayList<DoublyLinkedList> chipLines = new ArrayList<DoublyLinkedList>();
        //remove null lines
        for (DoublyLinkedList line : chipLines) {
            if (line == null) {
                chipLines.remove(line);
            }
        }
        iterateBoard(id, lineSize, chipLines, print);
        possibleMoves(); //update the possible list

        //make a new array for choices
        ArrayList<Integer> columnChoices = new ArrayList<Integer>();

        //check the head and tails of the linked lists to see the ids
        if (print && chipLines.isEmpty()) {
            System.out.println("Nothing in the chipLines");
            return null;
        }

        //for a special connection, check if the flag has been raised in one of the chipLines
        for (DoublyLinkedList line : chipLines) {
            if (line != null) {
                //check the connection flag on the head and tail
                if (line.head.info[3] == 1) {
                    columnChoices.add(line.head.info[1]);
                    if (print) System.out.println("The head is a connector at " + columnChoices);
                    return columnChoices;
                }
                if (line.tail.info[3] == 1) {
                    columnChoices.add(line.tail.info[1]);
                    if (print) System.out.println("The tail is a connector at " + columnChoices);
                    return columnChoices;
                }
            }
        }

        //there's no connector, get the column choices
        for (DoublyLinkedList line : chipLines) {
            if (line == null) {
                continue;
            }

            //get info to make it easier
            int[] headChip = {line.head.info[0], line.head.info[1], line.head.info[2]};
            int[] tailChip = {line.tail.info[0], line.tail.info[1], line.tail.info[2]};

            //for debug
            if (print) {
                System.out.println("Head Chip: " + headChip[0] + ", " + headChip[1]);
                System.out.println("Tail Chip: " + tailChip[0] + ", " + tailChip[1]);
            }


            //check if the head and tails are in the possibles list
            boolean headPossible = false;
            boolean tailPossible = false;
            for (int[] pos : possibles) {
                if (pos[0] == headChip[0] && pos[1] == headChip[1]) {
                    headPossible = true;
                }
                if (pos[0] == tailChip[0] && pos[1] == tailChip[1]) {
                    tailPossible = true;
                }
            }

            //if twoEmpties is true, we are looking for both the head and tail are empty
            //set the emptyCondition here before we add columns
            boolean emptyCondition = headChip[2] == 0 || tailChip[2] == 0;
            if (twoEmpties) {
                emptyCondition = headChip[2] == 0 && tailChip[2] == 0;
            }

            //both head and tail must be possible for the emptyCondition
            if (emptyCondition && headPossible && tailPossible && twoEmpties) {
                if (!columnChoices.contains(headChip[1])) {
                    columnChoices.add(headChip[1]);
                    if (print) {
                        System.out.println("Added " + headChip[1] + " to the column choices");
                    }
                }
                if (!columnChoices.contains(tailChip[1])) {
                    columnChoices.add(tailChip[1]);
                    if (print) {
                        System.out.println("Added " + tailChip[1] + " to the column choices");
                    }
                }


            } else if (emptyCondition) { //check either heads or tails
                if (headPossible) {
                    if (!columnChoices.contains(headChip[1])) {
                        columnChoices.add(headChip[1]);
                        if (print) {
                            System.out.println("Added " + headChip[1] + " to the column choices");
                        }
                    }
                }
                if (tailPossible) {
                    if (!columnChoices.contains(tailChip[1])) {
                        columnChoices.add(tailChip[1]);
                        if (print) {
                            System.out.println("Added " + tailChip[1] + " to the column choices");
                        }
                    }
                }
            }

            for (int[] pos : possibles) {
                if (emptyCondition) {
                    if (pos[0] == headChip[0] && pos[1] == headChip[1]) {
                        //if the column choice is already in, ignore this
                        if (!columnChoices.contains(headChip[1])) {
                            columnChoices.add(headChip[1]);
                            if (print) {
                                System.out.println("Added " + headChip[1] + " to the column choices");
                            }
                        }
                    }
                    if (pos[0] == tailChip[0] && pos[1] == tailChip[1]) {
                        //if the column is there, ignore this
                        if (!columnChoices.contains(tailChip[1])) {
                            columnChoices.add(tailChip[1]);
                            if (print) {
                                System.out.println("Added " + tailChip[1] + " to the column choices");
                            }
                        }
                    }
                }
            }

        }
        if (print) {
            System.out.print("found column choices: ");
            System.out.println(columnChoices);
            for (Integer i : columnChoices) {
                System.out.print((i + 1) + " ");
            }
            System.out.println();
            System.out.print("possible lists: ");
            for (int[] pos : possibles) {
                System.out.print(pos[0] + ", " + pos[1] + " | ");
            }
            System.out.println();
        }
        return columnChoices;
    }

    public static boolean nextMoveHasFour(int row, int col, int id) {
        ArrayList<DoublyLinkedList> linesWithFour = new ArrayList<DoublyLinkedList>();
        for (int direction = 0; direction < 4; direction++) { //there are four possible directions atm, so go through 4 iterations
            linesWithFour.add(findLine(row, col, id, 4, direction, false, false));
        }

        //check if there are any non null lines
        for (DoublyLinkedList line : linesWithFour) {
            if (line != null) {
                return true;
            }
        }
        return false;


    }

    public static int checkOutput(ArrayList<Integer> choices) {
        //find out how many lines there are
        int numberOfChoices = choices.size();

        ArrayList<Integer> removeChoice = new ArrayList<Integer>();

        if (numberOfChoices > 0) {
            //check possible choices to see if they would lose the game
            for (Integer columnChoice : choices) {
                for (int[] pos : possibles) {
                    //check if the columns match and that the next move is not off the board (row-1)
                    if (pos[0] - 1 >= 0 && pos[1] == columnChoice) {
                        //if the nextMoveLoses returns true, then you don't want to go there
                        if (nextMoveHasFour(pos[0] - 1, pos[1], 1)) {
                            removeChoice.add(columnChoice);
                        }
                    }
                }
            }
        }
        for (Integer rem : removeChoice) {
            if (choices.contains(rem)) {
                choices.remove(rem);
            }
        }
        //update the number of choices
        numberOfChoices = choices.size();

        if (numberOfChoices > 0) {
            //choose a random choice
            return choices.get((int) (Math.random() * numberOfChoices));
        }
        //give a -1 return if no results
        return -1;
    }

    //-----Bot Stuff------//
    public static int botMove(int id, int lineSize, boolean twoEmptiesNow, boolean oneEmptyAfter, boolean twoEmptiesAfter, boolean checkOutput, String message) {
        ArrayList<Integer> choices = new ArrayList<Integer>();
        //set a variable for the number of choices
        int numberOfChoices = 0;

        //initiate the checkOutResults to a default of -1
        int checkOutResults = -1;

        //set the default output
        int output = 0;

        //update possible moves list
        possibleMoves();

        //for the empty condition if specified
        boolean emptyCondition = false;

        //check the move in the parameter
        choices = findMove(id, lineSize, twoEmptiesNow, false);

        //find out how many lines there are
        numberOfChoices = choices.size();

        //if not checking the output and there are choices, take them and return.
        if (!checkOutput && numberOfChoices > 0) {
            if (debugMode) System.out.println(message);
            return choices.get((int) (Math.random() * numberOfChoices));
        }


        checkOutResults = checkOutput(choices);
        if ((oneEmptyAfter || twoEmptiesAfter) && checkOutResults != -1) {
            //check the possibles to get the coordinates of the move
            for (int[] pos : possibles) {
                if (pos[1] == checkOutResults) {
                    //run a line check
                    ArrayList<DoublyLinkedList> checkLines = new ArrayList<DoublyLinkedList>();
                    for (int direction = 0; direction < 4; direction++) { //there are four possible directions atm, so go through 4 iterations
                        checkLines.add(findLine(pos[0], checkOutResults, 2, 3, direction, false, false));
                    }
                    for (DoublyLinkedList line : checkLines) {
                        //check that the line isn't null and that both the head and tail have empty spaces
                        //update the empty boolean condition
                        if (line != null && twoEmptiesAfter) {
                            emptyCondition = (line.head.info[2] == 0 && line.tail.info[2] == 0);
                        } else if (line != null && oneEmptyAfter) {
                            emptyCondition = (line.head.info[2] == 0 || line.tail.info[2] == 0);
                        }
                        if (line != null && emptyCondition) {
                            if (debugMode) System.out.println(message);
                            return checkOutResults;
                        }
                    }
                }
            }
        } else if (checkOutResults != -1) {
            if (debugMode) System.out.println(message);
            return checkOutResults;
        }
        //if no move is found, return -1
        return -1;
    }

    public static int intBot() {

        //set the default output
        int output = -1;

        //check if middle of board is empty to make first move
        if (board[5][3] == 0) {
            if (debugMode) {
                System.out.println("Bot makes the first move in the middle");
            }
            output = 3;
            return output;
        }

        //check if the next move can win the game
        output = botMove(2, 3, false, false, false, false, "Bot moves to win");
        if (output != -1) return output;

        //check if the next winning move involves connecting a 2 to a 1 line
        findMove(2, 2, false, false);
        if (output != -1){
            System.out.println("filling the connection for the win");
            return output;
        }


        //check if defense is needed to block next move (player has three in a row)
        output = botMove(1, 3, false, false, false, false, "Bot moves to block a 3 sized line");
        if (output != -1) return output;

        //------from here we must check that the move will not result in a loss either-------//
        //check if can make a three with two empties after
        output = botMove(2, 2, true, false, true, true, "Bot moves to make a 3 with 2 empties, oh yeah");
        if (output != -1) return output;

        //check if can make a three with one empty after
        output = botMove(2, 2, true, true, false, true, "Bot moves to make a 3 with 2 empties, oh yeah");
        if (output != -1) return output;

        //check if can block a two with two empties
        output = botMove(1, 2, true, false, false, true, "Bot moves to block a 2 with 2 empties");
        if (output != -1) return output;

        //check if can make three with one empty
        output = botMove(2, 2, false, true, false, true, "Bot moves to make three with one empty");
        if (output != -1) return output;

        //check if can make a 2 with two empties
        output = botMove(2, 1, true, false, true, true, "Bot moves to make a 2 with 2 emmpties after");
        if (output != -1) return output;


        //check if can build near middle
        return buildMiddle();



    }

    public static int buildMiddle() {
        int[] order = new int[7];
        int randomOrder = (int)(Math.random()*6);
        if (randomOrder == 0) order = new int[] {3, 2, 4, 1, 5, 6, 0};
        if (randomOrder == 1) order = new int[] {3, 4, 2, 1, 5, 6, 0};
        if (randomOrder == 2) order = new int[] {3, 2, 4, 5, 1, 6, 0};
        if (randomOrder == 3) order = new int[] {3, 4, 2, 5, 1, 6, 0};
        if (randomOrder == 4) order = new int[] {3, 2, 4, 1, 5, 0, 6};
        if (randomOrder == 5) order = new int[] {3, 4, 2, 1, 5, 0, 6};
        //check if can build near middle
        for (int i =0; i < order.length; i++) {
            for (int[] pos : possibles) {
                if (pos[1] == order[i]) {
                    //check the next row up to see if it will make a winning move or if it's off the board
                    if ((pos[0] - 1 >= 0 && !nextMoveHasFour(pos[0] - 1, pos[1], 1)) || pos[0] - 1 < 0) {
                        if (debugMode) {
                            System.out.println("bot builds the mid at: " + pos[0] + ", " + pos[1]);
                        }
                        return order[i];
                    }
                }
            }


        }
        if (debugMode) System.out.println("choice of desperation");
        return order[6];
    }

}