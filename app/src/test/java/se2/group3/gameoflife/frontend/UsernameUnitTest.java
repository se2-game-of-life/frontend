package se2.group3.gameoflife.frontend;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import se2.group3.gameoflife.frontend.game.Username;

public class UsernameUnitTest {

    @ParameterizedTest
    @ValueSource(strings = {"Johanna01", "Thomas", "Flo007", "Anastasiia3", "Aya"})
    public void testCorrectUsernames(String name){
        assertTrue(Username.checkUsername(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "01", "70", "!", ":)", "Ana!", "0Jo"})
    public void testInCorrectUsernames(String name) {
        assertFalse(Username.checkUsername(name));
    }


}

