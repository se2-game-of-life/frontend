package se2.group3.gameoflife.frontend.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class PlayerDTO {
    @JsonSerialize
    private final String name;

    public PlayerDTO(String name) {
        this.name = name;
    }
}
