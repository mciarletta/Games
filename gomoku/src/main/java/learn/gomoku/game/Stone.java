package learn.gomoku.game;

import java.util.Objects;

public class Stone {

    private final int row;
    private final int column;
    private final boolean black;

    public Stone(int row, int column, boolean isBlack) {
        this.row = row;
        this.column = column;
        this.black = isBlack;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isBlack() {
        return black;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stone stone = (Stone) o;
        return row == stone.row && column == stone.column && black == stone.black;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, black);
    }

    @Override
    public String toString() {
        return "Stone{" +
                "row=" + row +
                ", column=" + column +
                ", black=" + black +
                '}';
    }
}
