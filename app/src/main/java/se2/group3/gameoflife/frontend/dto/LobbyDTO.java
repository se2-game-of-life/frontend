package se2.group3.gameoflife.frontend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import se2.group3.gameoflife.frontend.dto.cards.ActionCardDTODTO;
import se2.group3.gameoflife.frontend.dto.cards.CardDTO;
import se2.group3.gameoflife.frontend.dto.cards.CareerCardDTO;
import se2.group3.gameoflife.frontend.dto.cards.HouseCardDTO;

@JsonIgnoreProperties("stability")
public class LobbyDTO {

    private final long lobbyID;
    private final List<PlayerDTO> players;
    private final PlayerDTO currentPlayer;
    private boolean hasDecision;
    private List<ActionCardDTODTO> actionCardDTOs;
    private List<CareerCardDTO> careerCardDTOS;
    private List<HouseCardDTO> houseCardDTOS;
    private final int spunNumber;
    private final boolean hasStarted;

    @JsonCreator
    public LobbyDTO(@JsonProperty("lobbyID") long lobbyID,
                    @JsonProperty("players") List<PlayerDTO> players,
                    @JsonProperty("currentPlayer") PlayerDTO currentPlayer,
                    @JsonProperty("hasDecision") boolean hasDecision,
                    @JsonProperty("actionCards") List<ActionCardDTODTO> actionCards,
                    @JsonProperty("careerCards") List<CareerCardDTO> careerCards,
                    @JsonProperty("houseCards") List<HouseCardDTO> houseCards,
                    @JsonProperty("spunNumber") int spunNumber,
                    @JsonProperty("hasStarted") boolean hasStarted
    ) {
        this.lobbyID = lobbyID;
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.hasDecision = hasDecision;
        this.actionCardDTOs = actionCards;
        this.careerCardDTOS = careerCards;
        this.houseCardDTOS = houseCards;
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


    public List<CareerCardDTO> getCareerCardDTOS() {
        return careerCardDTOS;
    }

    public void setCareerCardDTOS(List<CareerCardDTO> careerCardDTOS) {
        this.careerCardDTOS = careerCardDTOS;
    }

    public List<ActionCardDTODTO> getActionCardDTOs() {
        return actionCardDTOs;
    }

    public void setActionCardDTOs(List<ActionCardDTODTO> actionCardDTOs) {
        this.actionCardDTOs = actionCardDTOs;
    }

    public List<HouseCardDTO> getHouseCardDTOS() {
        return houseCardDTOS;
    }

    public void setHouseCardDTOS(List<HouseCardDTO> houseCardDTOS) {
        this.houseCardDTOS = houseCardDTOS;
    }

    public int getSpunNumber() {
        return spunNumber;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }
}
