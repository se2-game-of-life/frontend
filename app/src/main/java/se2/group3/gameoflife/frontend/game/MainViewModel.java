package se2.group3.gameoflife.frontend.game;

import androidx.lifecycle.ViewModel;

import java.util.regex.Pattern;

/**
 * Used for the business logic of the MainActivity
 */
public class MainViewModel extends ViewModel {

    /**
     * method to check if username is valid
     * The username must consist of letters and can contain 0 or more digits at the end.
     * @return if the Username matches the necessary regex
     */
    public boolean checkUsername(String username){
        String usernameRegex = "^[a-zA-Z]+\\d*$";
        return Pattern.matches(usernameRegex, username);
    }

}
