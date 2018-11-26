package edu.umd.arturomcgill.healcity;

import java.util.ArrayList;

public class User
{
    public String email;
    public int totalSteps;
    public ArrayList<String> lifetimeAchievements;
    public ArrayList<String> friendIDs;
    public int lifetimeXP;


    public User()
    {

    }

    public User(String email)
    {
        this();
        this.email = email;
        lifetimeAchievements = new ArrayList<String>();
        friendIDs = new ArrayList<String>();
    }

    public User(String email, int totalSteps)
    {
        this(email);
        this.totalSteps = totalSteps;
    }

    public User(String email, String totalSteps)
    {
        this(email, Integer.parseInt(totalSteps));
    }

    public int getTotalSteps()
    {
        return totalSteps;
    }
}
