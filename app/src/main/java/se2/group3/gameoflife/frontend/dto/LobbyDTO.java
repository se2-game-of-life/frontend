package se2.group3.gameoflife.frontend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LobbyDTO {

    private final long lobbyID;
    private final PlayerDTO host;
    private final PlayerDTO[] players;

    @JsonCreator
    public LobbyDTO(@JsonProperty("lobbyID") long lobbyID, @JsonProperty("host") PlayerDTO host, @JsonProperty("players") PlayerDTO[] players) {
        this.lobbyID = lobbyID;
        this.host = host;
        this.players = players;
    }

    public long getLobbyID() {
        return this.lobbyID;
    }

    public PlayerDTO getHost() {
        return host;
    }

    public PlayerDTO[] getPlayers() {
        return players;
    }
}
