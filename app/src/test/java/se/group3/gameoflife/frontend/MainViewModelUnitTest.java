package se.group3.gameoflife.frontend;


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;



import se.group3.gameoflife.frontend.viewmodels.MainViewModel;

@RunWith(MockitoJUnitRunner.class)
class MainViewModelUnitTest {
    private MainViewModel mainViewModel;
    @BeforeEach
    public void setUp(){
        this.mainViewModel = new MainViewModel();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Johanna01", "Thomas", "Flo007", "Anastasiia3", "Aya"})
    void testCorrectUsernames(String name){
        mainViewModel.setUsername(name);
        assertTrue(mainViewModel.checkUsername());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "01", "70", "!", ":)", "Ana!", "0Jo"})
    void testInCorrectUsernames(String name) {
        mainViewModel.setUsername(name);
        assertFalse(mainViewModel.checkUsername());
    }


    @Test
    void testGetUsername(){
        mainViewModel.setUsername("Johanna");
        assertEquals("Johanna", mainViewModel.getUsername());
    }

    @AfterEach
    public void breakDown(){
        this.mainViewModel = null;
    }


}

