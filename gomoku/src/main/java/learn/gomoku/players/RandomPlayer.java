package learn.gomoku.players;

import learn.gomoku.game.Gomoku;
import learn.gomoku.game.Stone;

import java.util.List;
import java.util.Random;

public class RandomPlayer extends Bot implements Player {

    private final Random random = new Random();

    @Override
    public Stone generateMove(List<Stone> previousMoves) {

        boolean isBlack = true;
        if (previousMoves != null && !previousMoves.isEmpty()) {
            Stone lastMove = previousMoves.get(previousMoves.size() - 1);
            isBlack = !lastMove.isBlack();
        }

        return new Stone(
                random.nextInt(Gomoku.WIDTH),
                random.nextInt(Gomoku.WIDTH),
                isBlack);
    }
}


