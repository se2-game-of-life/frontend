package se2.group3.gameoflife.frontend.dto;

import java.util.List;

public class CellDTO {
    private int row;
    private int col;
    private String type;
    private int number;
    private List<Integer> nextCells; // Add the "nextCells" field

    public CellDTO() {
    }

    public CellDTO(int row, int col, String type, int number, List<Integer> nextCells) { // Update the constructor
        this.row = row;
        this.col = col;
        this.type = type;
        this.number = number;
        this.nextCells = nextCells;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() { // Add getter for "number"
        return number;
    }

    public void setNumber(int number) { // Add setter for "number"
        this.number = number;
    }

    public List<Integer> getNextCells() { // Add getter for "nextCells"
        return nextCells;
    }

    public void setNextCells(List<Integer> nextCells) { // Add setter for "nextCells"
        this.nextCells = nextCells;
    }
}
