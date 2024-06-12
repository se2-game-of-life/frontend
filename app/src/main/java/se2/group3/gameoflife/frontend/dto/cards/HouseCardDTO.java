package se2.group3.gameoflife.frontend.dto.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HouseCardDTO extends CardDTO {

    private String name;
    private int purchasePrice;
    private int redSellPrice;
    private int blackSellPrice;

    public HouseCardDTO(
                        @JsonProperty("name") String name,
                        @JsonProperty("purchasePrice") int purchasePrice,
                        @JsonProperty("redSellPrice") int redSellPrice,
                        @JsonProperty("blackSellPrice") int blackSellPrice) {
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.redSellPrice = redSellPrice;
        this.blackSellPrice = blackSellPrice;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getRedSellPrice() {
        return redSellPrice;
    }

    public void setRedSellPrice(int redSellPrice) {
        this.redSellPrice = redSellPrice;
    }

    public int getBlackSellPrice() {
        return blackSellPrice;
    }

    public void setBlackSellPrice(int blackSellPrice) {
        this.blackSellPrice = blackSellPrice;
    }
}
