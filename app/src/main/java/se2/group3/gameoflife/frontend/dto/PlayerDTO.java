package se2.group3.gameoflife.frontend.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import se2.group3.gameoflife.frontend.dto.cards.CareerCardDTO;
import se2.group3.gameoflife.frontend.dto.cards.HouseCardDTO;

@JsonIgnoreProperties("stability")
public class PlayerDTO {
    private String playerUUID;
    private Long lobbyID;
    private final String playerName;
    private int currentCellPosition;
    private int money;
    private CareerCardDTO careerCard;
    private int numberOfPegs;
    private List<HouseCardDTO> houses;
    private boolean collegeDegree;


    @JsonCreator
    public PlayerDTO(@JsonProperty("playerUUID") String playerUUID,
                     @JsonProperty("playerName") String playerName,
                     @JsonProperty("lobbyID") Long lobbyID,
                     @JsonProperty("currentCellPosition") int currentCellPosition,
                     @JsonProperty("money") int money,
                     @JsonProperty("careerCard") CareerCardDTO careerCard,
                     @JsonProperty("numberOfPegs") int numberOfPegs,
                     @JsonProperty("houses") List<HouseCardDTO> houses,
                     @JsonProperty("collageDegree") boolean collegeDegree
    ) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.lobbyID = lobbyID;
        this.currentCellPosition = currentCellPosition;
        this.money = money;
        this.careerCard = careerCard;
        this.numberOfPegs = numberOfPegs;
        this.houses = houses;
        this.collegeDegree = collegeDegree;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public int getCurrentCellPosition() {
        return currentCellPosition;
    }

    public int getMoney() {
        return money;
    }

    public int getNumberOfPegs() {
        return numberOfPegs;
    }

    public long getLobbyID() {
        return lobbyID;
    }

    public CareerCardDTO getCareerCard() {
        return careerCard;
    }

    public List<HouseCardDTO> getHouses() {
        return houses;
    }

    public boolean isCollegeDegree() {
        return collegeDegree;
    }
}

