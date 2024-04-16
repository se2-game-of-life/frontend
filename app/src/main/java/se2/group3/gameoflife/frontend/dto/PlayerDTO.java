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
    }
}

