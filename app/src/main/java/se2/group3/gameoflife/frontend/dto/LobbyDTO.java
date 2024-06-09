package se2.group3.gameoflife.frontend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import se2.group3.gameoflife.frontend.dto.cards.CardDTO;

@JsonIgnoreProperties("stability")
public class LobbyDTO {

    private final long lobbyID;
    private final List<PlayerDTO> players;
    private final PlayerDTO currentPlayer;
    private boolean hasDecision;
    private List<CardDTO> cardDTOS;
    private final int spunNumber;
    private final boolean hasStarted;

    @JsonCreator
    public LobbyDTO(@JsonProperty("lobbyID") long lobbyID,
                    @JsonProperty("players") List<PlayerDTO> players,
                    @JsonProperty("currentPlayer") PlayerDTO currentPlayer,
                    @JsonProperty("hasDecision") boolean hasDecision,
                    @JsonProperty("cards") List<CardDTO> cards,
                    @JsonProperty("spunNumber") int spunNumber,
                    @JsonProperty("hasStarted") boolean hasStarted
    ) {
        this.lobbyID = lobbyID;
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.hasDecision = hasDecision;
        this.cardDTOS = cards;
        this.spunNumber = spunNumber;
        this.hasStarted = hasStarted;
    }

    public long getLobbyID() {
        return this.lobbyID;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public PlayerDTO getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isHasDecision() {
        return hasDecision;
    }

    public List<CardDTO> getCards() {
        return cardDTOS;
    }

    public int getSpunNumber() {
        return spunNumber;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }
}
