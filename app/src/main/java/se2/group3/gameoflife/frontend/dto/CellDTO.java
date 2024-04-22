package se2.group3.gameoflife.frontend.dto;

public class CellDTO {
    private int row;
    private int col;
    private String type;

    public CellDTO() {
    }

    public CellDTO(int row, int col, String type) {
        this.row = row;
        this.col = col;
        this.type = type;
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
}
