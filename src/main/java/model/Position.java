package model;

public class Position {

    public int row;
    public int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }

    public boolean equals(Position position){
        return (this.getRow() == position.getRow() && this.getCol() == position.getCol());
    }

}
