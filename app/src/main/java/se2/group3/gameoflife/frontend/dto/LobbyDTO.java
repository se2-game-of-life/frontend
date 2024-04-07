package se2.group3.gameoflife.frontend.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class LobbyDTO {

    @JsonSerialize
    private long lobbyID;

    @JsonSerialize
    private PlayerDTO host;

    public LobbyDTO(long lobbyID, PlayerDTO host) {
        this.lobbyID = lobbyID;
        this.host = host;
    }

    public void setLobbyID(long lobbyID) {
        this.lobbyID = lobbyID;
    }

    public void setHost(PlayerDTO host) {
        this.host = host;
    }

}
