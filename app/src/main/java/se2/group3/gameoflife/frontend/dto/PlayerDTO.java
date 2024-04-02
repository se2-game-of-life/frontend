package se2.group3.gameoflife.frontend.dto;

public class PlayerDTO {
    private String username;

    public PlayerDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
