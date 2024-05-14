package se2.group3.gameoflife.frontend.dto;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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
    private boolean hasDecision;
    private List<Card> cards;
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
        Log.d("ParcelableDebug", "Reading lobbyID: " + lobbyID);

        players = in.createTypedArrayList(PlayerDTO.CREATOR);
        Log.d("ParcelableDebug", "Reading players: " + players);

        currentPlayer = in.readParcelable(PlayerDTO.class.getClassLoader());
        Log.d("ParcelableDebug", "Reading currentPlayer: " + currentPlayer);

//        hasDecision = in.readBoolean();
//        Log.d("ParcelableDebug", "Reading hasDecision: " + hasDecision);

//        cards = in.createTypedArrayList(Card.CREATOR);
//        Log.d("ParcelableDebug", "Reading cards: " + cards);

        spunNumber = in.readInt();
        Log.d("ParcelableDebug", "Reading spunNumber: " + spunNumber);

        hasStarted = in.readBoolean();
        Log.d("ParcelableDebug", "Reading hasStarted: " + hasStarted);
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
        Log.d("ParcelableDebug", "Writing lobbyID: " + lobbyID);
        dest.writeLong(lobbyID);

        Log.d("ParcelableDebug", "Writing players: " + players);
        dest.writeTypedList(players);

        Log.d("ParcelableDebug", "Writing currentPlayer: " + currentPlayer);
        dest.writeParcelable(currentPlayer, flags);

//        Log.d("ParcelableDebug", "Writing hasDecision: " + hasDecision);
//        dest.writeBoolean(hasDecision);

//        Log.d("ParcelableDebug", "Writing cards: " + cards);
//        dest.writeTypedList(cards);

        Log.d("ParcelableDebug", "Writing spunNumber: " + spunNumber);
        dest.writeInt(spunNumber);

        Log.d("ParcelableDebug", "Writing hasStarted: " + hasStarted);
        dest.writeBoolean(hasStarted);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
