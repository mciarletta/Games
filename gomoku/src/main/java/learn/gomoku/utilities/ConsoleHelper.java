package learn.gomoku.utilities;

import java.util.Scanner;

/**
 * The Console Helper acts as a I/O class to simplify input loops.
 * It also has a method for printing headers.
 */
public class ConsoleHelper {

    //the input Scanner to be used
    Scanner console = new Scanner(System.in);

    /**
     * Prints a message with a top and bottom border of ='s
     * @param message the message to be headed
     */
    public void heading(String message){
        System.out.println("=".repeat(message.length()));
        System.out.println(message);
        System.out.println("=".repeat(message.length()));

    }

    /**
     * Runs an input loop that continues until an input is not blank.
     * @param number If true, the input is looking for an integer.
     * @param from Begins the validation range of integer input (inclusive)
     * @param to End the validation range of integer input (inclusive).
     * @param message The message to be printed as a prompt to start each loop.
     * @return returns a String of the user's input.
     */
    public String prompt(boolean number, int from, int to, String message){
        String input;
        int numInput = from - 1;

        do {
            System.out.println();
            System.out.print(message);
            input = console.nextLine();
            if (input.isBlank()){
                System.out.println("Invalid input. Please try again.");
                continue;
            }
            if (number) {
                try {
                    numInput = Integer.parseInt(input);
                } catch (NumberFormatException nfe) {
                    System.out.printf("Invalid input.%n", from, to);
                    input = "";
                    continue;
                }
                if (numInput < from || numInput > to) {
                    System.out.printf("Please enter an integer from %s to %s.%n", from, to);
                    input = "";

                }
            }

        } while (input.isBlank());

        return input;
    }

    public String prompt(String message){
        return this.prompt(false, 0,0,message);
    }
    public String prompt(int from, int to, String message){
        return this.prompt(true, from,to,message);
    }
}
