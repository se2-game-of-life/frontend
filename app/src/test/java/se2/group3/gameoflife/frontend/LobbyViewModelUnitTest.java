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

import se2.group3.gameoflife.frontend.viewmodels.LobbyViewModel;

@RunWith(MockitoJUnitRunner.class)
class LobbyViewModelUnitTest {
    private LobbyViewModel lobbyViewModel;

    @BeforeEach
    public void setUp(){
        this.lobbyViewModel = new LobbyViewModel();
    }

    @Test
    void testSetLobbyDTO_exception(){
        LobbyDTO lobbyDTO = null;
        Exception ex = assertThrows(IllegalArgumentException.class, () -> lobbyViewModel.setLobbyDTO(lobbyDTO));
        assertEquals("LobbyDTO not found in the StartGameActivity", ex.getMessage());
    }

    @Test
    void testGetLobbyDTO_exception(){
        Exception ex = assertThrows(IllegalArgumentException.class, () -> lobbyViewModel.getLobbyDTO());
        assertEquals("LobbyDTO is null", ex.getMessage());
    }

    @Test
    void testSetLobbyDTO(){
        LobbyDTO lobbyDTO = new LobbyDTO(1L, null, null, false, null, null,null, 0, false);
        assertDoesNotThrow(() -> lobbyViewModel.setLobbyDTO(lobbyDTO));
    }

    @Test
    void testGetLobbyDTO(){
        LobbyDTO lobbyDTO = new LobbyDTO(1L, null, null, false, null, null, null,  0, false);
        lobbyViewModel.setLobbyDTO(lobbyDTO);
        assertDoesNotThrow(() -> lobbyViewModel.getLobbyDTO());
    }





    @AfterEach
    public void breakDown(){
        this.lobbyViewModel = null;
    }
}
