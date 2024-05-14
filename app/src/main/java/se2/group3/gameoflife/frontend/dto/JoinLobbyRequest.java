package se2.group3.gameoflife.frontend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinLobbyRequest {

    private final long lobbyID;
    private final String playerName;

    @JsonCreator
    public JoinLobbyRequest(@JsonProperty("lobbyID") long lobbyID, @JsonProperty("playerName") String playerName) {
        this.lobbyID = lobbyID;
        this.playerName = playerName;
    }

    public long getLobbyID() {
        return lobbyID;
    }

    public String getPlayerName() {
        return playerName;
    }
}