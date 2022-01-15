package top.hityx.cryptomall.models;

import java.util.Objects;

public class Good {
    private int goodID;
    private String goodName;
    private String goodDescription;
    private String goodImageName;
    private double goodPrice;
    private boolean goodOnSell;
    private String goodSellerUsername;

    public Good(int goodID, String goodName, String goodDescription, String goodImageName, double goodPrice, boolean goodOnSell, String goodSellerUsername) {
        this.goodID = goodID;
        this.goodName = goodName;
        this.goodDescription = goodDescription;
        this.goodImageName = goodImageName;
        this.goodPrice = goodPrice;
        this.goodOnSell = goodOnSell;
        this.goodSellerUsername = goodSellerUsername;
    }

    public int getGoodID() {
        return goodID;
    }

    public String getGoodName() {
        return goodName;
    }

    public String getGoodDescription() {
        return goodDescription;
    }

    public String getGoodImageName() {
        return goodImageName;
    }

    public double getGoodPrice() {
        return goodPrice;
    }

    public boolean isGoodOnSell() {
        return goodOnSell;
    }

    public String getGoodSellerUsername() {
        return goodSellerUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Good good = (Good) o;
        return goodID == good.goodID && Double.compare(good.goodPrice, goodPrice) == 0 && goodOnSell == good.goodOnSell && Objects.equals(goodName, good.goodName) && Objects.equals(goodDescription, good.goodDescription) && Objects.equals(goodImageName, good.goodImageName) && Objects.equals(goodSellerUsername, good.goodSellerUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodID, goodName, goodDescription, goodImageName, goodPrice, goodOnSell, goodSellerUsername);
    }

    @Override
    public String toString() {
        return "Good{" +
                "goodID=" + goodID +
                ", goodName='" + goodName + '\'' +
                ", goodDescription='" + goodDescription + '\'' +
                ", goodImageName='" + goodImageName + '\'' +
                ", goodPrice=" + goodPrice +
                ", goodOnSell=" + goodOnSell +
                ", goodSellerUsername='" + goodSellerUsername + '\'' +
                '}';
    }
}
