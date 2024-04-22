package se2.group3.gameoflife.frontend.dto;

import java.util.List;

public class BoardDTO {
    private List<List<CellDTO>> cells;

    public BoardDTO() {
    }

    public BoardDTO(List<List<CellDTO>> cells) {
        this.cells = cells;
    }

    public List<List<CellDTO>> getCells() {
        return cells;
    }

}
