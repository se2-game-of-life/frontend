package se2.group3.gameoflife.frontend.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties("stability")
public class PlayerDTO implements Parcelable {
    private final String playerName;
    private String playerID;
    private int currentCellPosition;
    private int money;
    private int investmentNumber; // The chosen investment number
    private int investmentLevel; // The current level of investment-
    private int numberOfPegs;
    //todo implement class CareerCard
    //private CareerCard careerCard;

    // Booleans for the different paths the game offers
    private boolean isCollegePath;
    private boolean isMarriedPath;
    private boolean isGrowFamilyPath;
    private boolean hasMidlifeCrisis;
    private boolean isRetireEarlyPath;


    @JsonCreator
    public PlayerDTO(@JsonProperty("playerName") String playerName) {
        this.playerName = playerName;
    }

    protected PlayerDTO(Parcel in) {
        playerName = in.readString();
    }

    public static final Creator<PlayerDTO> CREATOR = new Creator<PlayerDTO>() {
        @Override
        public PlayerDTO createFromParcel(Parcel in) {
            return new PlayerDTO(in);
        }

        @Override
        public PlayerDTO[] newArray(int size) {
            return new PlayerDTO[size];
        }
    };

    public String getPlayerName() {
        return this.playerName;
    }
  
      @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(playerName);
      
          public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public int getCurrentCellPosition() {
        return currentCellPosition;
    }

    public void setCurrentCellPosition(int currentCellPosition) {
        this.currentCellPosition = currentCellPosition;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getInvestmentNumber() {
        return investmentNumber;
    }

    public void setInvestmentNumber(int investmentNumber) {
        this.investmentNumber = investmentNumber;
    }

    public int getInvestmentLevel() {
        return investmentLevel;
    }

    public void setInvestmentLevel(int investmentLevel) {
        this.investmentLevel = investmentLevel;
    }

    public int getNumberOfPegs() {
        return numberOfPegs;
    }

    public void setNumberOfPegs(int numberOfPegs) {
        this.numberOfPegs = numberOfPegs;
    }

    public boolean isCollegePath() {
        return isCollegePath;
    }

    public void setCollegePath(boolean collegePath) {
        isCollegePath = collegePath;
    }

    public boolean isMarriedPath() {
        return isMarriedPath;
    }

    public void setMarriedPath(boolean marriedPath) {
        isMarriedPath = marriedPath;
    }

    public boolean isGrowFamilyPath() {
        return isGrowFamilyPath;
    }

    public void setGrowFamilyPath(boolean growFamilyPath) {
        isGrowFamilyPath = growFamilyPath;
    }

    public boolean isHasMidlifeCrisis() {
        return hasMidlifeCrisis;
    }

    public void setHasMidlifeCrisis(boolean hasMidlifeCrisis) {
        this.hasMidlifeCrisis = hasMidlifeCrisis;
    }

    public boolean isRetireEarlyPath() {
        return isRetireEarlyPath;
    }

    public void setRetireEarlyPath(boolean retireEarlyPath) {
        isRetireEarlyPath = retireEarlyPath;
    }
}

