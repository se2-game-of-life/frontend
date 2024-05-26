package se2.group3.gameoflife.frontend.dto;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import se2.group3.gameoflife.frontend.dto.cards.Card;
import se2.group3.gameoflife.frontend.dto.cards.CareerCard;
import se2.group3.gameoflife.frontend.dto.cards.HouseCard;

@JsonIgnoreProperties("stability")
public class PlayerDTO {
    private String playerUUID;
    private Long lobbyID;
    private final String playerName;
    private int currentCellPosition;
    private int money;
    private CareerCard careerCard;
    private int numberOfPegs;
    private List<Card> houses;
    private boolean collegeDegree;


    @JsonCreator
    public PlayerDTO(@JsonProperty("playerUUID") String playerUUID,
                     @JsonProperty("playerName") String playerName,
                     @JsonProperty("lobbyID") Long lobbyID,
                     @JsonProperty("currentCellPosition") int currentCellPosition,
                     @JsonProperty("money") int money,
                     @JsonProperty("careerCard") CareerCard careerCard,
                     @JsonProperty("numberOfPegs") int numberOfPegs,
                     @JsonProperty("houses") List<Card> houses,
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

    public CareerCard getCareerCard() {
        return careerCard;
    }

    public List<Card> getHouses() {
        return houses;
    }

    public boolean isCollegeDegree() {
        return collegeDegree;
    }
}

