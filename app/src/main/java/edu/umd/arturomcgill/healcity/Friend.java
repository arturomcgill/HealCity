package edu.umd.arturomcgill.healcity;

public class Friend {

    private String name;
    private int level;
    private int points;
//    private String description;

    public Friend() {
    }

    public Friend(String name, int level, int points) {
        this.name = name;
        this.level = level;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int  getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
}
