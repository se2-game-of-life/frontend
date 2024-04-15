package se2.group3.gameoflife.frontend.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LobbyDTO implements Parcelable {

    private final long lobbyID;
    private final PlayerDTO host;
    private final PlayerDTO[] players;

    @JsonCreator
    public LobbyDTO(@JsonProperty("lobbyID") long lobbyID, @JsonProperty("host") PlayerDTO host, @JsonProperty("players") PlayerDTO[] players) {
        this.lobbyID = lobbyID;
        this.host = host;
        this.players = players;
    }

    protected LobbyDTO(Parcel in) {
        lobbyID = in.readLong();
        host = in.readParcelable(PlayerDTO.class.getClassLoader());
        players = in.createTypedArray(PlayerDTO.CREATOR);
    }

    public static final Parcelable.Creator<LobbyDTO> CREATOR = new Parcelable.Creator<LobbyDTO>() {
        @Override
        public LobbyDTO createFromParcel(Parcel in) {
            return new LobbyDTO(in);
        }

        @Override
        public LobbyDTO[] newArray(int size) {
            return new LobbyDTO[0];
        }
    };

    public long getLobbyID() {
        return this.lobbyID;
    }

    public PlayerDTO getHost() {
        return host;
    }

    public PlayerDTO[] getPlayers() {
        return players;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(lobbyID);
        dest.writeParcelable(host, flags);
        dest.writeTypedArray(players, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
