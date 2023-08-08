import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        initBoard();
        newGame();
        drawBoard();
        while (!gameOver){
            beginRound();
        }
    }

    static int[][] board = {{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
    static String player1;
    static String player2;

    static Boolean player1first;
    static Boolean gameOver = false;
    static boolean bot1 = false;
    static boolean bot2 = false;



    public static void initBoard(){
        //zero out the 6 bottom rows
        for (int i = 0; i<=5; i++){
            for (int j=0; j<=6; j++){
                board[i][j] = 0;
            }
        }
    }

    public static void drawBoard(){
        System.out.println("1 2 3 4 5 6 7 ");
        for (int i = 0; i<=5; i++){
            for (int j=0; j<=6; j++){
                if (board[i][j] == 0) {
                    System.out.print("_ ");
                } else if (board[i][j] == 1) {
                    System.out.print("X ");
                } else {
                    System.out.print("0 ");
                }
                if (j == 6){
                    System.out.print("\n");
                }
            }
        }
    }

    public static void newGame(){
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Player #1, enter your name:");
        player1 = myScanner.nextLine();
        System.out.println("Hello, " + player1 + ".");

        String input = "";
        String message = "";
        while (true) {
            System.out.println("For player 2, press 1 for human, press 2 for simple random bot");
            input = myScanner.nextLine();

            if (input.equals("1")){
                System.out.println("Player #2, enter your name:");
                player2 = myScanner.nextLine();
                System.out.println("Hello, " + player2 + ".");
                break;

            } else if (input.equals("2")){
                player2 = "simple random Bot";
                System.out.println("Hello, " + player2 + ".");
                bot1 = true;
                break;
            } else {
                System.out.println("Please try again");
            }
        }




        System.out.println("(Randomizing...)");
        if (Math.random() > 0.5) {
            player1first = true;
            System.out.println("It's " + player1 + "'s turn.");
        } else {
            player1first = false;
            System.out.println("It's " + player2 + "'s turn.");

        }
    }

    public static void beginRound(){
        if (player1first){
            playerTurn(player1, 1);
        } else {
            playerTurn(player2, 2);
        }
        player1first = !player1first;
    }

    public static void playerTurn(String player, int id){
        String message = "";
        String input = "";
        int output;

        //validate the input

        if (id == 2 && bot1){
            while (true) {
                System.out.println(player + ", choose a column: ");
                output = (int) (Math.random() * 7);
                System.out.println(output+1);
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
                System.out.println(player + ", choose a column: ");

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

    public static void dropDisc(int col, int id){
        int cRow = 0;
        int cCol = 0;
        for (int i = 5; i >= 0; i--){
            if (board[i][col] == 0){
                board[i][col] = id;
                cRow = i;
                cCol = col;
                break;
            }
        }
        drawBoard();
        checkWinner(cCol,cRow, id);
    }

    public static void checkWinner(int col, int row, int id) {

        int count = 1;
        //check left/right
        for (int i = col-1; i >=0; i--){ //left
            if (board[row][i] == id){
                count++;
            } else {
                break;
            }
        }
        for (int i = col+1; i <= 6; i++){ //right
            if (board[row][i] == id){
                count++;
            } else {
                break;
            }
        }
        if (count >= 4){
            winner(id);
        }

        count = 1;
        //check up/down
        for (int i = row-1; i >=0; i--){ //up
            if (board[i][col] == id){
                count++;
            }else {
                break;
            }
        }
        for (int i = row+1; i <= 5; i++){ //down
            if (board[i][col] == id){
                count++;
            }else {
                break;
            }
        }
        if (count >= 4){
            winner(id);
        }

        count = 1;
        //check diagonal up/left - down/right
        for (int i = 1; (row-i>=0 && col-i>=0); i++){ //up/left
            if (board[row-i][col-i] == id){
                count++;
            }else {
                break;
            }
        }
        for (int i = 1; row+i<=5 && col+i<=6; i++){ //down/right
            if (board[row+i][col+i] == id){
                count++;
            }else {
                break;
            }
        }
        if (count >= 4){
            winner(id);
        }

        count = 1;
        //check diagonal up/right - down/left
        for (int i = 1; row-i>=0 && col+i<=6; i++){ //up/left
            if (board[row-i][col+i] == id){
                count++;
            }else {
                break;
            }
        }
        for (int i = 1; row+i<=5 && col-i>=0; i++){ //down/right
            if (board[row+i][col-i] == id){
                count++;
            }else {
                break;
            }
        }
        if (count >= 4){
            winner(id);
        }

        //check for a draw
        if (row == 0){
            for (int i=0; i<=6; i++){
                if (board[row][i] == 0) {
                    break;
                } else if (i==6 && board[row][i] != 0){
                    callDraw();
                }
            }
        }

    }

    public static void winner (int id){
        if (id == 1){
            System.out.println(player1 + " wins!");
        } else {
            System.out.println(player2 + " wins!");
        }
        playAgain();
    }

    public static void callDraw(){
        System.out.println("It's a draw!");
        playAgain();
    }

    public static void playAgain(){
        String input = "";
        String message = "";
        while (true) {
            System.out.println("Play again? [y/n]");
            Scanner myScanner = new Scanner(System.in);
            input = myScanner.nextLine();
            input =  input.toLowerCase();

            if (input.equals("y") || input.equals("n")){
                if (input.equals("y")){
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
}