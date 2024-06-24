package se2.group3.gameoflife.frontend;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import se2.group3.gameoflife.frontend.dto.BoardDTO;
import se2.group3.gameoflife.frontend.dto.CellDTO;
import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.viewmodels.GameBoardViewModel;

@RunWith(MockitoJUnitRunner.class)
class GameBoardViewModelUnitTest {
    private GameBoardViewModel gameBoardViewModel;

    @BeforeEach
    public void setUp(){
        this.gameBoardViewModel = new GameBoardViewModel();
    }

    @Test
    void testGetCellDTOHashMap() {
        Map<Integer, CellDTO> cellMap = gameBoardViewModel.getCellDTOHashMap();
        assertEquals(0, cellMap.size());
    }

    @Test
    void testSetCellDTOHashMap() {
        BoardDTO boardDTO = mock(BoardDTO.class);
        List<List<CellDTO>> cells = new ArrayList<>();
        List<CellDTO> row = new ArrayList<>();
        CellDTO cell1 = new CellDTO("1", 1,"ACTION", List.of(2),2,2);
        CellDTO cell2 = new CellDTO("2", 2,"FAMILY", List.of(3),2,4);
        row.add(cell1);
        row.add(cell2);
        cells.add(row);
        when(boardDTO.getCells()).thenReturn(cells);

        gameBoardViewModel.setCellDTOHashMap(boardDTO);
        Map<Integer, CellDTO> cellMap = gameBoardViewModel.getCellDTOHashMap();

        assertEquals(2, cellMap.size());
        assertEquals(cell1, cellMap.get(1));
        assertEquals(cell2, cellMap.get(2));
    }

    @Test
    void testSetCellDTOHashMap_null() {
        BoardDTO boardDTO = mock(BoardDTO.class);
        List<List<CellDTO>> cells = new ArrayList<>();
        List<CellDTO> row = new ArrayList<>();
        CellDTO cell1 = new CellDTO("1", 1,"ACTION", List.of(2),2,2);
        row.add(cell1);
        row.add(null);
        cells.add(row);
        when(boardDTO.getCells()).thenReturn(cells);

        gameBoardViewModel.setCellDTOHashMap(boardDTO);
        Map<Integer, CellDTO> cellMap = gameBoardViewModel.getCellDTOHashMap();

        assertEquals(1, cellMap.size());
        assertEquals(cell1, cellMap.get(1));
    }

    @Test
    void testGetOldLobbyDTO() {
        assertNull(gameBoardViewModel.getOldLobbyDTO());
    }

    @Test
    void testSetOldLobbyDTO() {
        LobbyDTO oldLobbyDTO = mock(LobbyDTO.class);
        gameBoardViewModel.setOldLobbyDTO(oldLobbyDTO);
        assertEquals(oldLobbyDTO, gameBoardViewModel.getOldLobbyDTO());
    }


    @AfterEach
    public void breakDown(){
        this.gameBoardViewModel = null;
    }

}
