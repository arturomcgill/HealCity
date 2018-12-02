package edu.umd.arturomcgill.healcity;

public class ShopItem {

    private String name;
    private int imgSrc;
    private int cost;
    private String description;

    public ShopItem() {
    }

    public ShopItem(String name, int imgSrc, int cost, String description) {
        this.name = name;
        this.imgSrc = imgSrc;
        this.cost = cost;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(int imgSrc) {
        this.imgSrc = imgSrc;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
