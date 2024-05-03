package se2.group3.gameoflife.frontend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import se2.group3.gameoflife.frontend.viewmodels.GameViewModel;


@RunWith(MockitoJUnitRunner.class)
public class GameViewModelUnitTest {

    private GameViewModel gameViewModel;

    @BeforeEach
    public void setUp(){
        this.gameViewModel = new GameViewModel();
    }

    @AfterEach
    public void breakDown(){
        this.gameViewModel = null;
    }

}
