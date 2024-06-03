package se2.group3.gameoflife.frontend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class CellDTO {
    private  String id;
    private String type;
    private int number;
    private List<Integer> nextCells;
    private int row;
    private int col;

    public CellDTO(@JsonProperty("id") String id,
                   @JsonProperty("number") int number,
                   @JsonProperty("type") String type,
                   @JsonProperty("nextCells") List<Integer> nextCells,
                   @JsonProperty("row") int row,
                   @JsonProperty("col") int col) {
        this.id = id;
        this.type = type;
        this.number = number;
        this.nextCells = nextCells;
        this.row = row;
        this.col = col;
    }

    public CellDTO() {
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public List<Integer> getNextCells() {
        return nextCells;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
