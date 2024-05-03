package se2.group3.gameoflife.frontend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import se2.group3.gameoflife.frontend.dto.LobbyDTO;
import se2.group3.gameoflife.frontend.dto.PlayerDTO;
import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;


@RunWith(MockitoJUnitRunner.class)
class GameViewModelUnitTest {

    private GameViewModel gameViewModel;

    @BeforeEach
    public void setUp(){
        this.gameViewModel = new GameViewModel();
    }
    @Test
    void testSetPlayerDTO_exception(){
        PlayerDTO playerDTO = null;
        Exception ex = assertThrows(IllegalArgumentException.class, () -> gameViewModel.setPlayerDTO(playerDTO));
        assertEquals("PlayerDTO is null", ex.getMessage());
    }

    @Test
    void testGetPlayerDTO_exception(){
        Exception ex = assertThrows(IllegalArgumentException.class, () -> gameViewModel.getPlayerDTO());
        assertEquals("PlayerDTO is null", ex.getMessage());
    }

    @Test
    void testSetPlayerDTO(){
        assertDoesNotThrow(() -> gameViewModel.setPlayerDTO(new PlayerDTO("Player")));
        assertEquals("Player", gameViewModel.getPlayerDTO().getPlayerName());
    }

    @Test
    void testGetPlayerDTO(){
        gameViewModel.setPlayerDTO(new PlayerDTO("Player"));
        assertDoesNotThrow(() -> gameViewModel.getPlayerDTO());
    }

    @AfterEach
    public void breakDown(){
        this.gameViewModel = null;
    }

}
