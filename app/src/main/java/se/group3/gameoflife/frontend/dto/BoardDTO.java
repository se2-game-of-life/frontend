package se.group3.gameoflife.frontend.dto;

import androidx.annotation.NonNull;

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

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BoardDTO{");
        sb.append("cells=[");
        for (List<CellDTO> row : cells) {
            sb.append("[");
            for (CellDTO cell : row) {
                sb.append(cell != null ? cell.toString() : "null");
                sb.append(", ");
            }
            sb.append("], ");
        }
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }
}
