package learn.gomoku.players;

import learn.gomoku.game.Stone;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ThoughtfulPlayerTest {
    private static int EM = 0;
    private static int BL = 2;
    private static int WH = 1;

    ThoughtfulPlayer instance = new ThoughtfulPlayer();

    @Test
    void generateMoveList(){
        int[][] board = new int[][]{
                {0,0,0,0,0,0},
                {0,0,1,1,0,0},
                {0,0,1,1,0,0},
                {0,0,1,1,0,0},
                {2,0,0,2,0,0},
                {0,0,0,0,0,0}
               };

        ArrayList<int[]> possibles = instance.generatePossibleMoves(board);
        ArrayList<LinkedList<int[]>> blackMoves =  instance.checkLines(possibles, board, 2);
        System.out.println("----------------------------------------------------");
        ArrayList<LinkedList<int[]>> whiteMoves =  instance.checkLines(possibles, board, 1);
        assertNotNull(blackMoves);
        assertNotNull(whiteMoves);

        //white should make a move that creates a line of 4 with a potential 5
        int[] actual = instance.findMove(whiteMoves);
        assertNotNull(actual);

        actual = instance.findMove(blackMoves);
        assertNotNull(actual);


    }

/*
    @Test
    void shouldBlock3With2EmptiesOnCrowdedBoard(){
        int[][] board = new int[15][15];
        board[0][1] = BL;
        board[0][2] = BL;
        board[0][3] = BL;
        board[0][4] = BL;
        ArrayList<int[]> possibles = instance.generatePossibleMoves(board);
        Stone actual = instance.checkLines(possibles, board,BL, 5, false, true, WH);
        Stone expected = new Stone(0, 0, false);
        assertEquals(actual, expected, actual.toString());

    }
    @Test
    void shouldBlock4With2Empties(){
        int[][] board = new int[15][15];
        board[0][1] = BL;
        board[0][2] = BL;
        board[0][3] = BL;
        board[0][4] = BL;
        ArrayList<int[]> possibles = instance.generatePossibleMoves(board);
        Stone actual = instance.checkLines(possibles, board,BL, 5, false, true, WH);
        Stone expected = new Stone(0, 0, false);
        assertEquals(actual, expected, actual.toString());

    }

    @Test
    void shouldBlock3With2Empties(){
        int[][] board = new int[15][15];
        board[0][1] = BL;
        board[0][2] = BL;
        board[0][3] = BL;
        ArrayList<int[]> possibles = instance.generatePossibleMoves(board);
        Stone actual = instance.checkLines(possibles, board,BL, 4, false, true, WH);
        Stone expected = new Stone(0, 0, false);
        assertEquals(actual, expected, actual.toString());

    }

    @Test
    void fourShouldBlockWinningStoneIfPossible(){
        int[][] board = new int[15][15];
        board[0][0] = WH;
        board[0][1] = BL;
        board[0][2] = BL;
        board[0][3] = BL;
        board[0][4] = BL;
        ArrayList<int[]> possibles = instance.generatePossibleMoves(board);
        Stone actual = instance.checkLines(possibles, board,BL, 5, false, false, WH);
        Stone expected = new Stone(0, 5, false);
        assertEquals(actual, expected);

    }

    @Test
    void fourShouldMakeWinningStoneIfPossible(){
        int[][] board = new int[15][15];
        board[0][0] = BL;
        board[0][1] = BL;
        board[0][2] = BL;
        board[0][3] = BL;
        ArrayList<int[]> possibles = instance.generatePossibleMoves(board);
        Stone actual = instance.checkLines(possibles, board, BL, 5, false, false, BL);
        Stone expected = new Stone(0, 4, true);
        assertEquals(actual, expected);

    }

    @Test
    void overFiveWontMakeWinningStone(){
        int[][] board = new int[15][15];
        board[0][0] = BL;
        board[0][1] = BL;
        board[0][2] = BL;
        board[0][3] = BL;
        board[0][4] = BL;
        ArrayList<int[]> possibles = instance.generatePossibleMoves(board);
        Stone actual = instance.checkLines(possibles, board, BL, 5,false,false,BL);
        Stone expected = null;
        assertEquals(actual, expected);


        board[0][5] = BL;
        board[0][4] = EM;
        actual = instance.checkLines(possibles, board, BL, 5,false,false,BL);
        assertEquals(actual, expected);

    }

 */

}