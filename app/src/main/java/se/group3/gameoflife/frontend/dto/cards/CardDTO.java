package se.group3.gameoflife.frontend.dto.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDTO {
    //this class will stay empty, as it is only used to group the other DTOs
    @JsonCreator
    public CardDTO() {
        //empty constructor
    }
}
