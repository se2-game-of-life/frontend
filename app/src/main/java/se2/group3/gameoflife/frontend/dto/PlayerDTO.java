package se2.group3.gameoflife.frontend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerDTO {
    private final String playerName;

    @JsonCreator
    public PlayerDTO(@JsonProperty("playerName") String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return this.playerName;
    }
}

