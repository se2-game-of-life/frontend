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

import se2.group3.gameoflife.frontend.viewmodels.StartGameViewModel;

@RunWith(MockitoJUnitRunner.class)
class StartGameViewModelUnitTest {
    private StartGameViewModel startGameViewModel;

    @BeforeEach
    public void setUp(){
        this.startGameViewModel = new StartGameViewModel();
    }

    @Test
    void testSetLobbyDTO_exception(){
        LobbyDTO lobbyDTO = null;
        Exception ex = assertThrows(IllegalArgumentException.class, () -> startGameViewModel.setLobbyDTO(lobbyDTO));
        assertEquals("LobbyDTO not found in the StartGameActivity", ex.getMessage());
    }

    @Test
    void testGetLobbyDTO_exception(){
        Exception ex = assertThrows(IllegalArgumentException.class, () -> startGameViewModel.getLobbyDTO());
        assertEquals("LobbyDTO is null", ex.getMessage());
    }

    @Test
    void testSetLobbyDTO(){
        LobbyDTO lobbyDTO = new LobbyDTO(1L, null, null, false, null, 0, false);
        assertDoesNotThrow(() -> startGameViewModel.setLobbyDTO(lobbyDTO));
    }

    @Test
    void testGetLobbyDTO(){
        LobbyDTO lobbyDTO = new LobbyDTO(1L, null, null, false, null, 0, false);
        startGameViewModel.setLobbyDTO(lobbyDTO);
        assertDoesNotThrow(() -> startGameViewModel.getLobbyDTO());
    }





    @AfterEach
    public void breakDown(){
        this.startGameViewModel = null;
    }
}
