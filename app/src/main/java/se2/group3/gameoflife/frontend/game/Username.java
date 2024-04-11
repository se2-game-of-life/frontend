package se2.group3.gameoflife.frontend.game;

import java.util.regex.Pattern;

public class Username {

    /**
     * method to check if username is valid
     * The username must consist of letters and can contain 0 or more digits at the end.
     * @param username: Username entered by player
     * @return if the Username matches the necessary regex
     */
    public static boolean checkUsername(String username){
        String usernameRegex = "^[a-zA-Z]+\\d*$";
        return Pattern.matches(usernameRegex, username);
    }
}
