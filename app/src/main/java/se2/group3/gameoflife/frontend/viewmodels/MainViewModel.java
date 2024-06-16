package se2.group3.gameoflife.frontend.viewmodels;

import androidx.lifecycle.ViewModel;

import java.util.regex.Pattern;

public class MainViewModel extends ViewModel {

    private String username = null;

    /**
     * method to check if username is valid
     * The username must consist of letters and can contain 0 or more digits at the end.
     * @return if the Username matches the necessary regex
     */
    public boolean checkUsername(){
        String usernameRegex = "^[a-zA-Z]+\\d*$";
        return Pattern.matches(usernameRegex, username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
