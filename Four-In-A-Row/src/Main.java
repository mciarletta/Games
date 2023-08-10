import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        initBoard();
        newGame();
        drawBoard();
        while (!gameOver) {
            beginRound();
        }
    }

    static int[][] board = {{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
    static String player1;
    static String player2;

    static Boolean player1first;
    static Boolean gameOver = false;
    static boolean bot1 = false;
    static boolean bot2 = false;

    static List<int[]> possibles;
    static List<int[]> checkedMoves;
    static ArrayList<DoublyLinkedList> checkedLines;
    static ArrayList<DoublyLinkedList> testLines;


    public static void initBoard() {
        //zero out the 6 bottom rows
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 6; j++) {
                board[i][j] = 0;
            }
        }
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
    }

    public static void newGame() {
        Scanner myScanner = new Scanner(System.in);
        System.out.print("Player #1, enter your name: ");
        player1 = myScanner.nextLine();
        System.out.println("Hello, " + player1 + ".\n");

        String input = "";
        String message = "";
        while (true) {
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
        player1first = !player1first;
    }

    public static void playerTurn(String player, int id) {
        String message = "";
        String input = "";
        int output = 0;

        if (id == 2 && bot2) {
            System.out.print(player + ", choose a column: ");
            output = intBot();
            System.out.println(output+1);

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

            while (true) {
                System.out.print(player + ", choose a column: ");

                Scanner myScanner = new Scanner(System.in);
                input = myScanner.nextLine();

                if (input.equals("q")) {
                    System.out.println("Thanks for playing! Goodbye");
                    System.exit(0);
                }
                try {
                    output = Integer.parseInt(input) - 1;
                } catch (NumberFormatException nfe) {
                    message = ("Not and integer. Please input a valid column number integer");
                    System.out.println(message);
                    continue;
                }
                if (board[0][output] != 0) {
                    message = ("Full. Please input a valid column number that is not full");
                    System.out.println(message);
                    continue;
                }
                break;


            }
        }
        //call a method to drop a disc
        dropDisc(output, id);
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

        int count = 1;
        //check left/right
        for (int i = col - 1; i >= 0; i--) { //left
            if (board[row][i] == id) {
                count++;
            } else {
                break;
            }
        }
        for (int i = col + 1; i <= 6; i++) { //right
            if (board[row][i] == id) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 4) {
            winner(id);
        }

        count = 1;
        //check up/down
        for (int i = row - 1; i >= 0; i--) { //up
            if (board[i][col] == id) {
                count++;
            } else {
                break;
            }
        }
        for (int i = row + 1; i <= 5; i++) { //down
            if (board[i][col] == id) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 4) {
            winner(id);
        }

        count = 1;
        //check diagonal up/left - down/right
        for (int i = 1; (row - i >= 0 && col - i >= 0); i++) { //up/left
            if (board[row - i][col - i] == id) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; row + i <= 5 && col + i <= 6; i++) { //down/right
            if (board[row + i][col + i] == id) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 4) {
            winner(id);
        }

        count = 1;
        //check diagonal up/right - down/left
        for (int i = 1; row - i >= 0 && col + i <= 6; i++) { //up/left
            if (board[row - i][col + i] == id) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; row + i <= 5 && col - i >= 0; i++) { //down/right
            if (board[row + i][col - i] == id) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 4) {
            winner(id);
        }

        //check for a draw
        if (row == 0) {
            for (int i = 0; i <= 6; i++) {
                if (board[row][i] == 0) {
                    break;
                } else if (i == 6 && board[row][i] != 0) {
                    callDraw();
                }
            }
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

    public static int intBot() {
        int output = 0;

        //update possible moves list
        possibleMoves(2, 0);

        //check if middle of board is empty to make first move
        if (board[5][3] == 0) {
            output = 3;
            return output;
        }

        //check if the next move can win the game
        for (int i = 0; i <= 5; i++) { //check rows
            for (int j = 0; j <= 6; j++) { //check cols
                if (board[i][j] == 2) { //there is a bot chip, check the length
                    checkLines(j, i, 2, 3); //update the lines to see if there are any moves to complete a chain of 3

                    for (DoublyLinkedList check : checkedLines){ //check the head and tails of the linked lists to see the ids
                        if (check.head == null){
                            continue;
                        }
                        int headID = check.head.info[2];
                        int tailID = check.tail.info[2];
                        if (headID == 0){ //grab the col info to make the move
                            //make sure that the position of the place is possible
                            int[] head = {check.head.info[0], check.head.info[1]};
                            int[] tail = {check.tail.info[0], check.tail.info[1]};
                            if (possibles.contains(head)){
                                return check.head.info[1];
                            } else if (possibles.contains(tail)){
                                return check.tail.info[1];
                            } else {
                            }
                        }
                    }
                }
            }
        }


        //check if defense is needed to block next move (player has three in a row)
        for (int i = 0; i <= 5; i++) { //check rows
            for (int j = 0; j <= 6; j++) { //check cols
                if (board[i][j] == 1) { //there is a player chip, check the length
                    checkLines(j, i, 1, 3); //update the checkedMoved to see if there are any moves to block a chain of 3

                    for (DoublyLinkedList check : checkedLines){ //check the head and tails of the linked lists to see the ids
                        if (check.head == null){
                            continue;
                        }
                        int headID = check.head.info[2];
                        int tailID = check.tail.info[2];
                        if (headID == 0){ //grab the col info to make the move
                            return check.head.info[1];
                        } else if (tailID == 0) {
                            return check.tail.info[1];
                        }

                    }
                }
            }
        }

        //from here we must check that the move will not result in a loss either

        //check if can make three with two empties
        for (int i = 0; i <= 5; i++) { //check rows
            for (int j = 0; j <= 6; j++) { //check cols
                if (board[i][j] == 2) { //there is a bot chip, check the length
                    checkLines(j, i, 2, 2); //update the checkedLines

                    for (DoublyLinkedList check : checkedLines) { //check the head and tails of the linked lists to see the ids
                        if (check.head == null){
                            continue;
                        }
                        if(check.head.info != null) {
                            int headID = check.head.info[2]; //causing a  null pointer exception
                            int tailID = check.tail.info[2];
                            if (headID == 0 && tailID == 0) { //grab the col info to make the move
                                if (!willLose(check.head.info[0], check.head.info[1])){
                                    return check.head.info[1];
                                }
                                if (!willLose(check.tail.info[0], check.tail.info[1])){
                                    return check.tail.info[1];
                                }
                            }
                        }
                    }
                }
            }
        }

        //check if can block a three with two empties
        for (int i = 0; i <= 5; i++) { //check rows
            for (int j = 0; j <= 6; j++) { //check cols
                if (board[i][j] == 1) {
                    checkLines(j, i, 1, 2); //update the checkedLines

                    for (DoublyLinkedList check : checkedLines) { //check the head and tails of the linked lists to see the ids
                        if (check.head == null){
                            continue;
                        }
                        int headID = check.head.info[2];
                        int tailID = check.tail.info[2];
                        if (headID == 0 && tailID == 0) { //grab the col info to make the move
                            if (!willLose(check.head.info[0], check.head.info[1])){
                                return check.head.info[1];
                            }
                            if (!willLose(check.tail.info[0], check.tail.info[1])){
                                return check.tail.info[1];
                            }
                        }
                    }
                }
            }
        }



        //check if can build near middle
        ArrayList cols = new ArrayList();
        for (int[] pos : possibles) {//iterate through the possible and add the col choices to an array
            if (!willLose(pos[0], pos[1])){
                cols.add(pos[1]);
            }
            cols.add(pos[1]);
        }
        if (cols.contains(3)){
            return 3;
        } else if (cols.contains(2)) {
            return 2;
        } else if (cols.contains(4)) {
            return 4;
        } else if (cols.contains(1)) {
            return 1;
        } else if (cols.contains(5)) {
            return 5;
        } else if (cols.contains(0)) {
            return 0;
        } else if (cols.contains(6)) {
            return 6;
        }

        //all else fails, just fill empty spot
        while (true) {
            int rando = (int) (Math.random() * 7);
            if (board[0][rando] != 0) {
                continue;
            } else {
                output = rando;
                break;
            }
        }


        return output;


    }

    public static void possibleMoves(int id, int max) {
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

    public static void checkChipLength(int col, int row, int id, int countMax) {
        //note 1 implies true 0 implies false

        int nRow = 0;
        int nCol = 0;
        int nnRow = 0;
        int nnCol = 0;
        boolean found1 = false;
        boolean found2 = false;

        //create an array to hold the coordinates of the checked moves to be compared to possible moves later
        checkedMoves = new ArrayList<int[]>();

        int count = 1;
        //check left/right
        for (int i = col - 1; i >= 0; i--) { //left
            if (board[row][i] == id) { //there's a chip, continue
                count++;
            } else if (board[row][i] == 0) {//there's no chip, the line ends.
                nRow = row;
                nCol = i;
                found1 = true;
                break;
            }
        }
        for (int i = col + 1; i <= 6; i++) { //right
            if (board[row][i] == id) {
                count++;
            } else if (board[row][i] == 0) {
                nnRow = row;
                nnCol = i;
                found2 = true;
                break;
            }
        }
        if (count >= countMax) {
            if (found1) {
                checkedMoves.add(new int[]{nRow, nCol});
            }
            if (found2) {
                checkedMoves.add(new int[]{nnRow, nnCol});
            }
        }

        count = 1;
        found1 = false;
        found2 = false;
        //check up/down
        for (int i = row - 1; i >= 0; i--) { //up
            if (board[i][col] == id) {
                count++;
            } else if (board[i][col] == 0) {
                found1 = true;
                nRow = i;
                nCol = col;
                break;
            }
        }
        for (int i = row + 1; i <= 5; i++) { //down
            if (board[i][col] == id) {
                count++;
            } else if (board[i][col] == 0) {
                found2 = true;
                nnRow = i;
                nnCol = col;
                break;
            }
        }
        if (count >= countMax) {
            if (found1) {
                checkedMoves.add(new int[]{nRow, nCol});
            }
            if (found2) {
                checkedMoves.add(new int[]{nnRow, nnCol});
            }
        }

        count = 1;
        found1 = false;
        found2 = false;
        //check diagonal up/left - down/right
        for (int i = 1; (row - i >= 0 && col - i >= 0); i++) { //up/left
            if (board[row - i][col - i] == id) {
                count++;
            } else if (board[row - i][col - i] == 0) {
                found1 = true;
                nRow = row - i;
                nCol = col - i;
                break;
            }
        }
        for (int i = 1; row + i <= 5 && col + i <= 6; i++) { //down/right
            if (board[row + i][col + i] == id) {
                count++;
            } else if (board[row + i][col + i] == 0) {
                found2 = true;
                nnRow = row + i;
                nnCol = col + i;
                break;
            }
        }
        if (count >= countMax) {
            if (found1) {
                checkedMoves.add(new int[]{nRow, nCol});
            }
            if (found2) {
                checkedMoves.add(new int[]{nnRow, nnCol});
            }
        }

        count = 1;
        found1 = false;
        found2 = false;
        //check diagonal up/right - down/left
        for (int i = 1; row - i >= 0 && col + i <= 6; i++) { //up/left
            if (board[row - i][col + i] == id) {
                count++;
            } else if (board[row - i][col + i] == 0) {
                found1 = true;
                nRow = row - i;
                nCol = col + i;
                break;
            }
        }
        for (int i = 1; row + i <= 5 && col - i >= 0; i++) { //down/right
            if (board[row + i][col - i] == id) {
                count++;
            } else if (board[row + i][col - i] == 0) {
                found2 = true;
                nnRow = row + i;
                nnCol = col - i;
                break;
            }
        }
        if (count >= countMax) {
            if (found1) {
                checkedMoves.add(new int[]{nRow, nCol});
            }
            if (found2) {
                checkedMoves.add(new int[]{nnRow, nnCol});
            }
        }


    }

    public static void checkLines(int col, int row, int id, int countMax){
        //create an array to hold the coordinates of the checked moves to be compared to possible moves later
        checkedLines =  new ArrayList<DoublyLinkedList>();
        int count = 0;

        //check left/right
        count = 1;
        DoublyLinkedList leftRight = new DoublyLinkedList();
        for (int i = col - 1; i >= 0; i--) { //left
            if (board[row][i] == id) { //there's a chip, add it to the doulbylinked list
                int[] chipInfo = {row, i, id};
                leftRight.insertFront(chipInfo);
                count++;
            } else if (board[row][i] == 0) {//there's no chip, the line ends.
                int[] chipInfo = {row, i, 0};
                leftRight.insertFront(chipInfo); //put the blank space on the list for later
                break;
            } else {
                break;
            }
        }
        for (int i = col + 1; i <= 6; i++) { //right
            if (board[row][i] == id) {
                int[] chipInfo = {row, i, id};
                leftRight.insertBack(chipInfo);
                count++;
            } else if (board[row][i] == 0) {
                int[] chipInfo = {row, i, 0};
                leftRight.insertBack(chipInfo);
                break;
            } else {
                break;
            }
        }
        if (count >= countMax) {
            checkedLines.add(leftRight);
        }

        //check up/down
        count = 1;
        DoublyLinkedList upDown = new DoublyLinkedList();
        for (int i = row - 1; i >= 0; i--) { //up
            if (board[i][col] == id) {
                int[] chipInfo = {i, col, id};
                upDown.insertFront(chipInfo);
                count++;
            } else if (board[i][col] == 0) {
                int[] chipInfo = {i, col, 0};
                upDown.insertFront(chipInfo);
                break;
            } else {
                break;
            }
        }
        for (int i = row + 1; i <= 5; i++) { //down
            if (board[i][col] == id) {
                int[] chipInfo = {i, col, id};
                upDown.insertBack(chipInfo);
                count++;
            } else if (board[i][col] == 0) {
                int[] chipInfo = {i, col, 0};
                upDown.insertBack(chipInfo);
                break;
            } else {
                break;
            }
        }
        if (count >= countMax) {
            checkedLines.add(upDown);
        }

        //check diagonal up/left - down/right
        count = 1;
        DoublyLinkedList diagUpLeft = new DoublyLinkedList();
        for (int i = 1; (row - i >= 0 && col - i >= 0); i++) { //up/left
            if (board[row - i][col - i] == id) {
                int[] chipInfo = {row-i, col-i, id};
                diagUpLeft.insertFront(chipInfo);
                count++;
            } else if (board[row - i][col - i] == 0) {
                int[] chipInfo = {row-i, col-i, 0};
                diagUpLeft.insertFront(chipInfo);
                break;
            } else {
                break;
            }
        }
        for (int i = 1; row + i <= 5 && col + i <= 6; i++) { //down/right
            if (board[row + i][col + i] == id) {
                int[] chipInfo = {row+i, col+i, id};
                diagUpLeft.insertBack(chipInfo);
                count++;
            } else if (board[row + i][col + i] == 0) {
                int[] chipInfo = {row+i, col+i, 0};
                diagUpLeft.insertBack(chipInfo);
                break;
            } else {
                break;
            }
        }
        if (count >= countMax) {
            checkedLines.add(diagUpLeft);
        }



        //check diagonal up/right - down/left
        count = 1;
        DoublyLinkedList diagUpRight = new DoublyLinkedList();
        for (int i = 1; row - i >= 0 && col + i <= 6; i++) { //up/right
            if (board[row - i][col + i] == id) {
                int[] chipInfo = {row-i, col+i, id};
                diagUpRight.insertFront(chipInfo);
                count++;
            } else if (board[row - i][col + i] == 0) {
                int[] chipInfo = {row-i, col+i, 0};
                diagUpRight.insertFront(chipInfo);
                break;
            } else {
                break;
            }
        }
        for (int i = 1; row + i <= 5 && col - i >= 0; i++) { //down/left
            if (board[row + i][col - i] == id) {
                int[] chipInfo = {row+i, col-i, id};
                diagUpRight.insertBack(chipInfo);
                count++;
            } else if (board[row + i][col - i] == 0) {
                int[] chipInfo = {row+i, col-i, id};
                diagUpRight.insertBack(chipInfo);
                break;
            } else {
                break;
            }
        }
        if (count >= countMax) {
            checkedLines.add(diagUpRight);
        }

        //for testing
        testLines = new ArrayList<DoublyLinkedList>();
        testLines.add(leftRight);
        testLines.add(upDown);
        testLines.add(diagUpLeft);
        testLines.add(diagUpRight);





    }

    public static boolean willLose(int row, int col){
        for (int i = 0; i <= 5; i++) { //check rows
            for (int j = 0; j <= 6; j++) { //check cols
                if (board[i][j] == 1) { //there is a player
                    checkLines(j, i, 2, 3); //update the checkedLines

                    for (DoublyLinkedList check : checkedLines) { //check the head and tails of the linked lists to see the ids
                        if (check.head == null){
                            continue;
                        }
                        int headID = check.head.info[2];
                        if (headID == 0 && (row-1) == check.head.info[0] && col == check.head.info[1]){ //check the pos to make sure that it isn't one above where the move will be played
                            return false;
                        }
                        int tailID = check.tail.info[2];
                        if (tailID == 0 && (row-1) == check.tail.info[0] && col == check.tail.info[1]){ //check the pos to make sure that it isn't one above where the move will be played
                            return false;
                        }


                    }
                }
            }
        }
        return true;
    }


}