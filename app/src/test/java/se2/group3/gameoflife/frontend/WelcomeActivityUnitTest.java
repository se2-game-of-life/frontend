package se2.group3.gameoflife.frontend;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class WelcomeActivityUnitTest {
    private WelcomeActivity activity;


    @BeforeEach
    public void setUp() {
        activity = new WelcomeActivity();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Johanna01", "Thomas", "Flo007", "Anastasiia3", "Aya"})
    public void testCorrectUsernames(String name){
        assertTrue(activity.checkUsername(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "01", "70", "!", ":)", "Ana!", "0Jo"})
    public void testInCorrectUsernames(String name) {
        assertFalse(activity.checkUsername(name));
    }

    @AfterEach
    public void breakDown(){
        activity = null;
    }

}

