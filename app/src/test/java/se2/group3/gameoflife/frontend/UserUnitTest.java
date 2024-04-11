package se2.group3.gameoflife.frontend;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import se2.group3.gameoflife.frontend.game.User;

class UserUnitTest {
    private User user;
    @BeforeEach
    public void setUp(){
        this.user = new User();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Johanna01", "Thomas", "Flo007", "Anastasiia3", "Aya"})
    void testCorrectUsernames(String name){
        user.setUsername(name);
        assertTrue(user.checkUsername());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "01", "70", "!", ":)", "Ana!", "0Jo"})
    void testInCorrectUsernames(String name) {
        user.setUsername(name);
        assertFalse(user.checkUsername());
    }

    @AfterEach
    public void breakDown(){
        this.user = null;
    }


}

