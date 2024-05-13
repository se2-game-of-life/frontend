package se2.group3.gameoflife.frontend.dto;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import se2.group3.gameoflife.frontend.dto.cards.Card;

@JsonIgnoreProperties("stability")
public class LobbyDTO implements Parcelable {

    private final long lobbyID;
    private final List<PlayerDTO> players;
    private final PlayerDTO currentPlayer;
    private final boolean hasDecision;
    private final List<Card> cards;
    private final int spunNumber;
    private final boolean hasStarted;

    @JsonCreator
    public LobbyDTO(@JsonProperty("lobbyID") long lobbyID,
                    @JsonProperty("players") List<PlayerDTO> players,
                    @JsonProperty("currentPlayer") PlayerDTO currentPlayer,
                    @JsonProperty("hasDecision") boolean hasDecision,
                    @JsonProperty("cards") List<Card> cards,
                    @JsonProperty("spunNumber") int spunNumber,
                    @JsonProperty("hasStarted") boolean hasStarted
    ) {
        this.lobbyID = lobbyID;
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.hasDecision = hasDecision;
        this.cards = cards;
        this.spunNumber = spunNumber;
        this.hasStarted = hasStarted;
    }

    protected LobbyDTO(Parcel in) {
        lobbyID = in.readLong();
        players = in.createTypedArrayList(PlayerDTO.CREATOR);
        currentPlayer = in.readParcelable(PlayerDTO.class.getClassLoader());
        hasDecision = in.readBoolean();
        cards = in.createTypedArrayList(Card.CREATOR);
        spunNumber = in.readInt();
        hasStarted = in.readBoolean();
    }

    public static final Parcelable.Creator<LobbyDTO> CREATOR = new Parcelable.Creator<LobbyDTO>() {
        @Override
        public LobbyDTO createFromParcel(Parcel in) {
            return new LobbyDTO(in);
        }

        @Override
        public LobbyDTO[] newArray(int size) {
            return new LobbyDTO[size];
        }
    };

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

    public List<Card> getCards() {
        return cards;
    }

    public int getSpunNumber() {
        return spunNumber;
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(lobbyID);
        dest.writeTypedList(players);
        dest.writeParcelable(currentPlayer, flags);
        dest.writeBoolean(hasDecision);
        dest.writeTypedList(cards);
        dest.writeInt(spunNumber);
        dest.writeBoolean(hasStarted);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
