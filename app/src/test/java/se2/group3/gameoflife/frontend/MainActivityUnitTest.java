package se2.group3.gameoflife.frontend;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MainActivityUnitTest {
    private MainActivity activity;


    @BeforeEach
    public void setUp() {
        activity = new MainActivity();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Johanna01", "Thomas", "Flo007", "Anastasiia3", "Aya"})
    public void testCorrectUsernames(String name){
        assertTrue(activity.checkUsername(name));
    }


    @AfterEach
    public void breakDown(){
        activity = null;
    }

}

