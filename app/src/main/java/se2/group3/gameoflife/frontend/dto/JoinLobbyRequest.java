package se2.group3.gameoflife.frontend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinLobbyRequest {

    private long lobbyID;
    private PlayerDTO player;

    @JsonCreator
    public JoinLobbyRequest(@JsonProperty("lobbyID") long lobbyID, @JsonProperty("player") PlayerDTO player) {
        this.lobbyID = lobbyID;
        this.player = player;
    }

    public long getLobbyID() {
        return lobbyID;
    }

    public PlayerDTO getPlayer() {
        return player;
    }
}