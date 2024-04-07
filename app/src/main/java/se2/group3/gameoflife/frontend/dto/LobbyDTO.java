package se2.group3.gameoflife.frontend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LobbyDTO {
    private long lobbyID;

    private PlayerDTO host;

    @JsonCreator
    public LobbyDTO(@JsonProperty("lobbyID") long lobbyID, @JsonProperty("host") PlayerDTO host) {
        this.lobbyID = lobbyID;
        this.host = host;
    }

    public long getLobbyID() {
        return this.lobbyID;
    }

    public PlayerDTO getHost() {
        return this.host;
    }

}
