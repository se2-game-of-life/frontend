package se.group3.gameoflife.frontend.viewmodels;

import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

import se.group3.gameoflife.frontend.dto.BoardDTO;
import se.group3.gameoflife.frontend.dto.CellDTO;
import se.group3.gameoflife.frontend.dto.LobbyDTO;

public class GameBoardViewModel extends ViewModel {

    private final HashMap<Integer, CellDTO> cellDTOHashMap = new HashMap<>();
    private LobbyDTO oldLobbyDTO;

    public Map<Integer, CellDTO> getCellDTOHashMap() {
        return cellDTOHashMap;
    }

    public void setCellDTOHashMap(BoardDTO boardDTO) {
        for (int row = 0; row < boardDTO.getCells().size(); row++) {
            for (int col = 0; col < boardDTO.getCells().get(row).size(); col++) {
                CellDTO cell = boardDTO.getCells().get(row).get(col);
                if(cell == null) continue;
                cellDTOHashMap.put(cell.getNumber(), cell);
            }
        }
    }

    public LobbyDTO getOldLobbyDTO() {
        return oldLobbyDTO;
    }

    public void setOldLobbyDTO(LobbyDTO oldLobbyDTO) {
        this.oldLobbyDTO = oldLobbyDTO;
    }
}
