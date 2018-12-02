package edu.umd.arturomcgill.healcity;

import java.util.ArrayList;
import java.util.List;

public class Friend {

    private String email;
    private int level;
    private int points;
    private ArrayList<String> achievements = new ArrayList<>();
//    private String description;

    public Friend() {
    }

    public Friend(String email, int level, int points) {
        this.email = email;
        this.level = level;
        this.points = points;

        achievements.add("Beginner");

        for (int i = 0; i < level; i++) {
            achievements.add("Reached level " + i);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String name) {
        this.email = email;
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

    public ArrayList<String> getAchievements() {
        return achievements;
    }

    public void addAchievement(String achievement) {
        achievements.add(achievement);
    }

    //    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
}
