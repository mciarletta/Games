package learn.gomoku.players;

import learn.gomoku.game.Gomoku;
import learn.gomoku.game.Stone;

import java.util.*;

public class ThoughtfulPlayer extends Bot implements Player {

    static int EM = 0;
    static int WH = 1;
    static int BL = 2;

    static int PP = -1;
    static int NE = 3;

    private final Random random = new Random();

    private enum Direction {RIGHT_LEFT, UP_DOWN, UP_LEFT, UP_RIGHT};

    @Override
    public Stone generateMove(List<Stone> previousMoves) {
        //set some variable names for convenience


        boolean isBlack = true;
        if (previousMoves != null && !previousMoves.isEmpty()) {
            Stone lastMove = previousMoves.get(previousMoves.size() - 1);
            isBlack = !lastMove.isBlack();
        }
        //find out my color
        int myColor = (isBlack) ? BL : WH;
        int oppColor = myColor == BL ? WH : BL;

        //make a new array
        int[][] boardData = new int[15][15];
        //fill it with EMs
        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                boardData[row][col] = EM;
            }

        }
        //put the stone data into the boarData
        assert previousMoves != null;
        for (Stone s : previousMoves) {
            int row = s.getRow();
            int col = s.getColumn();
            int colorNum = (s.isBlack()) ? BL : WH;
            boardData[row][col] = colorNum;
        }

        //get a list of possible moves
        ArrayList<int[]> possibles = generatePossibleMoves(boardData);

        //make a list to hold all of the possible my moves
        ArrayList<LinkedList<int[]>> myMoves = checkLines(possibles, boardData, myColor);

        //make a list to hold all of the possible opposition moves
        ArrayList<LinkedList<int[]>> oppMoves = checkLines(possibles, boardData, oppColor);

        //generate the move
        int[] offense = findMove(myMoves);
        int[] defense = findMove(oppMoves);

       if (offense != null && defense != null) {
           //make winning move
           if (offense[2] == 5){
               return new Stone(offense[0], offense[1], isBlack);
           } else if (defense[2] == 5){
               return new Stone(defense[0], defense[1], isBlack);

           } //alternate between offense and defensive moves
           if (offense[2] > defense[2]) {
               return new Stone(offense[0], offense[1], isBlack);
           } else if (offense[2] == defense[2]) {
               if (offense[3] >= defense[3]) {
                   return new Stone(offense[0], offense[1], isBlack);
               }
           } else {
               return new Stone(defense[0], defense[1], isBlack);

           }
       }
       if (offense == null && defense != null){
           return new Stone(defense[0], defense[1], isBlack);
       }
       if (offense != null && defense == null){
           return new Stone(offense[0], offense[1], isBlack);

       }

        //--Offense place a stone from the center out

        //center coordinates
        int cenRow = 7;
        int cenCol = 7;

        //check if the center is open
        for (int[] p : possibles) {
            if (p[0] == cenRow && p[1] == cenCol) {
                System.out.println("Placing in the center");

                return new Stone(p[0], p[1], isBlack);
            }
        }

        //check for open moves from the center out
        for (int delta = 0; delta < 8; delta++) {
            int mode = 0;
            while (mode < 8) {
                switch (mode) {
                    case 0:
                        for (int[] p : possibles) {
                            if (p[0] == cenRow + delta && p[1] == cenCol - delta) {
                                return new Stone(p[0], p[1], isBlack);
                            }
                        }
                        break;
                    case 1:
                        for (int[] p : possibles) {
                            if (p[0] == cenRow && p[1] == cenCol - delta) {
                                return new Stone(p[0], p[1], isBlack);
                            }
                        }
                        break;
                    case 2:
                        for (int[] p : possibles) {
                            if (p[0] == cenRow - delta && p[1] == cenCol - delta) {
                                return new Stone(p[0], p[1], isBlack);
                            }
                        }
                        break;
                    case 3:
                        for (int[] p : possibles) {
                            if (p[0] == cenRow + delta && p[1] == cenCol) {
                                return new Stone(p[0], p[1], isBlack);
                            }
                        }
                        break;
                    case 4:
                        for (int[] p : possibles) {
                            if (p[0] == cenRow - delta && p[1] == cenCol) {
                                return new Stone(p[0], p[1], isBlack);
                            }
                        }
                        break;
                    case 5:
                        for (int[] p : possibles) {
                            if (p[0] == cenRow + delta && p[1] == cenCol + delta) {
                                return new Stone(p[0], p[1], isBlack);
                            }
                        }
                        break;
                    case 6:
                        for (int[] p : possibles) {
                            if (p[0] == cenRow && p[1] == cenCol + delta) {
                                return new Stone(p[0], p[1], isBlack);
                            }
                        }
                        break;
                    case 7:
                        for (int[] p : possibles) {
                            if (p[0] == cenRow - delta && p[1] == cenCol + delta) {
                                return new Stone(p[0], p[1], isBlack);
                            }
                        }
                        break;

                }
                mode++;
            }
        }


        //all else fails, and it shouldn't, return a random stone
        System.out.println("going random, even though it shouldnt");

        return new Stone(
                random.nextInt(Gomoku.WIDTH),
                random.nextInt(Gomoku.WIDTH),
                isBlack);
    }

    private LinkedList<int[]> makeLines(int[][] board, int row, int col, int id, Direction direction){

        LinkedList<int[]> line = new LinkedList<>();
        line.addFirst(new int[]{row, col, id, 0, PP});



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
            case RIGHT_LEFT: //left-right
                frontInit = col - 1;
                frontCondition = frontInit >= 0;
                frontIterator = -1;
                fCol = frontInit;

                backInit = col + 1;
                backCondition = backInit < board.length;
                backIterator = 1;
                bCol = backInit;
                code = "left-right";
                break;
            case UP_DOWN: //up-down
                frontInit = row - 1;
                frontCondition = frontInit >= 0;
                frontIterator = -1;
                fRow = frontInit;

                backInit = row + 1;
                backCondition = backInit < board.length;
                backIterator = 1;
                bRow = backInit;
                code = "up-down";
                break;
            case UP_LEFT: //up-left
                frontInit = 1;
                frontCondition = (row - frontInit >= 0 && col - frontInit >= 0);
                frontIterator = 1;
                fRow = row - frontInit;
                fCol = col - frontInit;

                backInit = 1;
                backCondition = row + backInit < board.length && col + backInit < board.length;
                backIterator = 1;
                bRow = row + backInit;
                bCol = col + backInit;
                code = "up-left";
                break;
            case UP_RIGHT: //up-right
                frontInit = 1;
                frontCondition = row - frontInit >= 0 && col + frontInit < board.length;
                frontIterator = 1;
                fRow = row - frontInit;
                fCol = col + frontInit;

                backInit = 1;
                backCondition = row + backInit < board.length && col - backInit >= 0;
                backIterator = 1;
                bRow = row + backInit;
                bCol = col - backInit;
                code = "up-right";
                break;
        }


        for (; frontCondition; frontInit += frontIterator) { //front links
            //update the info
            switch (direction) {
                case RIGHT_LEFT: //left-right
                    frontCondition = frontInit > 0;
                    fCol = frontInit;
                    break;
                case UP_DOWN: //up-down
                    frontCondition = frontInit > 0;
                    fRow = frontInit;
                    break;
                case UP_LEFT: //up-left
                    frontCondition = row - frontInit > 0 && col - frontInit > 0;
                    fRow = row - frontInit;
                    fCol = col - frontInit;
                    break;
                case UP_RIGHT: //up-right
                    frontCondition = row - frontInit > 0 && col + frontInit < 6;
                    fRow = row - frontInit;
                    fCol = col + frontInit;
                    break;
            }

            if (board[fRow][fCol] == id) { //there's a chip, add it to the doulbylinked list
                //if we aren't checking the connection, increase the count and add the chip
                int[] chipInfo = {fRow, fCol, id, 0, 0};
                line.addFirst(chipInfo);

            } else if (board[fRow][fCol] == EM) {//there's no chip, the line ends.
                //add the blank chip info
                int[] temp = line.get(0);
                temp[3] = NE;
                line.set(0, temp);
                break;

            } else {
                break;
            }
        }

        for (; backCondition; backInit += backIterator) { //back links
            //update the info
            switch (direction) {
                case RIGHT_LEFT: //left-right
                    backCondition = backInit < board.length-1;
                    bCol = backInit;
                    break;
                case UP_DOWN: //up-down
                    backCondition = backInit < board.length-1;
                    bRow = backInit;
                    break;
                case UP_LEFT: //up-left
                    backCondition = row + backInit < board.length-1 && col + backInit < board.length-1;
                    bRow = row + backInit;
                    bCol = col + backInit;
                    break;
                case UP_RIGHT: //up-right
                    backCondition = row + backInit < board.length-1 && col - backInit > 0;
                    bRow = row + backInit;
                    bCol = col - backInit;
                    break;
            }

            if (board[bRow][bCol] == id) {
                //if we aren't checking the connection, increase the count and add the chip
                int[] chipInfo = {bRow, bCol, id, 0, 0};
                line.addLast(chipInfo);

            } else if (board[bRow][bCol] == EM) {
                if (!line.isEmpty()) {
                    int[] temp = line.get(line.size() - 1);
                    temp[3] = NE;
                    line.set(line.size() - 1, temp);
                }
                break;
            } else {
                //there's nothing there, break
                break;
            }
        }

//        System.out.println("from: " + row + ", " + col + " " + code);
//        for (int[] i : line){
//            System.out.println(Arrays.toString(i));
//        }

        return line;
    }

    public ArrayList<LinkedList<int[]>> checkLines(ArrayList<int[]> possibles, int[][] board, int color) {

        LinkedList<int[]> line;
        ArrayList<LinkedList<int[]>> lines = new ArrayList<>();

        for (int[] pos : possibles) {

            for (Direction direction: Direction.values()){
                line = makeLines(board, pos[0], pos[1], color, direction);
                if (line.size() > 1){
                    lines.add(line);
                }

            }

        }

        return lines;
    }

    public ArrayList<int[]> generatePossibleMoves(int[][] boardData) {
        ArrayList<int[]> possibles = new ArrayList<>();
        for (int row = 0; row < boardData.length; row++) {
            for (int col = 0; col < boardData.length; col++) {
                if (boardData[row][col] == EM) {
                    possibles.add(new int[]{row, col});
                }
            }
        }
        return possibles;
    }

    public int[] findMove(ArrayList<LinkedList<int[]>> lines) {
        ArrayList<int[]> stones = new ArrayList<>();
        int[] stone;
//        int emptyCode = 2; //0 is no empties, 1 is 1 empty, 2 is two empties
        for (int size = 5; size > 1; size--) {

            for (int emptyCode = 2; emptyCode >= 0; emptyCode--) {

                for (LinkedList<int[]> line : lines) {
                    if (line.size() == size) {
                        //check the front and back for empties
                        int[] head = line.get(0);
                        int[] tail = line.get(line.size() - 1);
                        int[] placement = new int[5];
                        if (emptyCode == 0 && (head[3] == 0 && tail[3] == 0)) {
                            //find the PP
                            for (int[] node : line) {
                                int[] temp = node;
                                if (temp[4] == -1) {
                                    placement = temp;
                                }
                            }
                            stones.add(new int[]{placement[0], placement[1], 0, 0});
                        }
                        if (emptyCode == 2 && (head[3] == 3 && tail[3] == 3)) {
                            //find the PP
                            for (int[] node : line) {
                                int[] temp = node;
                                if (temp[4] == -1) {
                                    placement = temp;
                                }
                            }
                            stones.add(new int[]{placement[0], placement[1], 0, 0});
                        }
                        if (emptyCode == 1 && (head[3] == 3 || tail[3] == 3)) {
                            //find the PP
                            for (int[] node : line) {
                                int[] temp = node;
                                if (temp[4] == -1) {
                                    placement = temp;
                                }
                            }
                            stones.add(new int[]{placement[0], placement[1], 0, 0});
                        }
                    }
                }


                //if there are stones by the end of the emptyCode loop, those are the best, we can stop here
                if (!stones.isEmpty()) {
                    for (int[] s : stones) {
                        s[2] = size;
                        s[3] = emptyCode;
                    }
                    break;
                }
            }//end emptyCode loop
            if (!stones.isEmpty()) {
                break;
            }
        }//end size loop

        //pull a random stone out
        if (!stones.isEmpty()) {
            int rando = (int) (Math.random() * stones.size());
            return stones.get(rando);
        }

        return null;
    }


}
