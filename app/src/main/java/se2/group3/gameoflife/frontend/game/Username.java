package se2.group3.gameoflife.frontend.game;

import java.util.regex.Pattern;

public class Username {

    /**
     * method to check if username is valid
     * The username must consist of letters and can contain 0 or more digits at the end.
     */
    public static boolean checkUsername(String username){
        String usernameRegex = "^[a-zA-Z]+[0-9]*$";
        return Pattern.matches(usernameRegex, username);
    }
}
