package edu.umd.arturomcgill.healcity;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class User
{
    private String email;
    private String uid;
    private int totalSteps;
    private Bitmap profilePhoto;
    private ArrayList<String> lifetimeAchievements;
    private ArrayList<String> friendEmails;
    private ArrayList<String> upgradesPurchased;
    private ArrayList<String> goalsSet;
    private ArrayList<String> goalsMet;
    private HashMap<Date, Integer> dailyXP;
    private HashMap<Date, Integer> dailyFruitsVeggies;
    private HashMap<Date, Integer> dailySteps;
    private int lifetimeParks;
    private int lifetimeVolunteering;
    private int lifetimePublicTransportation;
    private String firstName;
    private String lastName;
    private int points;


    public User()
    {
        lifetimeAchievements = new ArrayList<String>();
        friendEmails = new ArrayList<String>();
        totalSteps = 0;
        profilePhoto = null;
        upgradesPurchased = new ArrayList<String>();
        goalsMet = new ArrayList<String>();
        goalsSet = new ArrayList<String>();
        dailyXP = new HashMap<Date, Integer>();
        dailySteps = new HashMap<Date, Integer>();
        dailyFruitsVeggies = new HashMap<Date, Integer>();
        firstName = "NoFirstName";
        lastName ="NoLastName";
    }

    public User(String email, String uid, String firstName, String lastName)
    {
        this();
        this.email = email;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String email, String uid)
    {
        this(email, uid, "NoFirstName", "NoLastName");
    }

    public String getEmail()
    {
        return email;
    }


    public String getUid()
    {
        return uid;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public ArrayList<String> getLifetimeAchievements()
    {
        return lifetimeAchievements;
    }

    public void setLifetimeAchievements(ArrayList<String> lifetimeAchievements)
    {
        this.lifetimeAchievements = lifetimeAchievements;
    }

    public int getLifetimeSteps()
    {
        if(dailySteps.size() == 0)
            return 0;
        else
        {
            int sum = 0;

            for(Map.Entry<Date, Integer> entry : dailySteps.entrySet())
            {
                sum += entry.getValue();
            }

            return sum;
        }
    }

    public int getLifetimeFruitsVeggies()
    {
        if(dailyFruitsVeggies.size() == 0)
            return 0;
        else
        {
            int sum = 0;

            for(Map.Entry<Date, Integer> entry : dailyFruitsVeggies.entrySet())
            {
                sum += entry.getValue();
            }

            return sum;
        }
    }

    public void resetLifetimeAchievements()
    {
        this.lifetimeAchievements = new ArrayList<String>();
    }


    public void addLifetimeAchievement(String newAchievement)
    {
        lifetimeAchievements.add(newAchievement);
    }

    public ArrayList<String> getFriendEmails()
    {
        return friendEmails;
    }

    public void setFriendEmails(ArrayList<String> friendEmails)
    {
        this.friendEmails = friendEmails;
    }

    public void resetFriendEmails()
    {
        friendEmails = new ArrayList<String>();
    }

    public void addFriendEmail(String email)
    {
        friendEmails.add(email);
    }

    public int getTotalSteps()
    {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps)
    {
        this.totalSteps = totalSteps;
    }

    public void resetTotalSteps()
    {
        totalSteps = 0;
    }

    public void addTotalSteps(int steps)
    {
        totalSteps += steps;
    }


    public int getLifetimeVolunteering()
    {
        return lifetimeVolunteering;
    }

    public void setLifetimeVolunteering(int lifetimeVolunteering)
    {
        this.lifetimeVolunteering = lifetimeVolunteering;
    }

    public void resetLifetimeVolunteering()
    {
        lifetimeVolunteering = 0;
    }

    public void addlifetimeVolunteering(int volunteerHours)
    {
        lifetimeVolunteering += volunteerHours;
    }


    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public void resetPoints()
    {
        points = 0;
    }

    public void addPoints(int points)
    {
        this.points += points;
    }


    public int getlifetimePublicTransportation()
    {
        return lifetimePublicTransportation;
    }

    public void setlifetimePublicTransportation(int lifetimePublicTransportation)
    {
        this.lifetimePublicTransportation = lifetimePublicTransportation;
    }

    public void resetlifetimePublicTransportation()
    {
        lifetimePublicTransportation = 0;
    }

    public void addlifetimePublicTransportation(int publicTransport)
    {
        lifetimePublicTransportation += publicTransport;
    }


    public int getlifetimeParks()
    {
        return lifetimeParks;
    }

    public void setlifetimeParks(int lifetimeParks)
    {
        this.lifetimeParks = lifetimeParks;
    }

    public void resetlifetimeParks()
    {
        lifetimeParks = 0;
    }

    public void addlifetimeParks(int parks)
    {
        lifetimeParks += parks;
    }

    public int getLifetimeXP()
    {
        if(dailyXP.size() == 0)
            return 0;
        else
        {
            int sum = 0;

            for(Map.Entry<Date, Integer> entry : dailyXP.entrySet())
            {
                sum += entry.getValue();
            }

            return sum;
        }
    }

    public Bitmap getProfilePhoto()
    {
        return profilePhoto;
    }

    public void setProfilePhoto(Bitmap photo)
    {
        this.profilePhoto = photo;
    }

    public void resetProfilePhoto()
    {

        this.profilePhoto = null;
    }

    public ArrayList<String> getUpgradesPurchased()
    {
        return upgradesPurchased;
    }


    public void setUpgradesPurchased(ArrayList<String> upgradesPurchased)
    {
        this.upgradesPurchased = upgradesPurchased;
    }

    public void resetUpgradesPurchased()
    {
        this.upgradesPurchased = new ArrayList<String>();
    }

    public void addUpgradesPurchased(String upgrade)
    {
        this.upgradesPurchased.add(upgrade);
    }

    public ArrayList<String> getGoalsMet()
    {
        return goalsMet;
    }

    public void setGoalsMet(ArrayList<String> goalsMet)
    {
        this.goalsMet = goalsMet;
    }

    public void resetGoalsMet()
    {
        this.goalsMet = new ArrayList<String>();
    }

    public void addGoalsMet(String goalMet)
    {
        this.goalsMet.add(goalMet);
    }

    public ArrayList<String> getGoalsSet()
    {
        return goalsSet;
    }

    public void setGoalsSet(ArrayList<String> goalsSet)
    {
        this.goalsSet = goalsSet;
    }

    public void resetGoalsSet()
    {
        this.goalsSet = new ArrayList<String>();
    }

    public void addGoalsSet(String goalSet)
    {
        this.goalsSet.add(goalSet);
    }

    public HashMap<Date, Integer> getDailyXP()
    {
        return dailyXP;
    }

    public void setDailyXP(HashMap<Date, Integer> dailyXP)
    {
        this.dailyXP = dailyXP;
    }

    public void resetDailyXP()
    {
        this.dailyXP = new HashMap<Date, Integer>();
    }

    public void addDailyXPToDate(Date date, int xp)
    {
        this.dailyXP.put(date, xp);
    }

    public HashMap<Date, Integer> getDailySteps()
    {
        return dailySteps;
    }

    public void setDailySteps(HashMap<Date, Integer> dailySteps)
    {
        this.dailySteps = dailySteps;
    }

    public void resetDailySteps()
    {
        this.dailySteps = new HashMap<Date, Integer>();
    }

    public void addDailystepsToDate(Date date, int steps)
    {
        this.dailySteps.put(date, steps);
    }

    public HashMap<Date, Integer> getFruitsVeggies()
    {
        return dailyFruitsVeggies;
    }

    public void setFruitsVeggies(HashMap<Date, Integer> dailyFruitsVeggies)
    {
        this.dailyFruitsVeggies = dailyFruitsVeggies;
    }

    public void resetFruitsVeggies()
    {
        this.dailyFruitsVeggies = new HashMap<Date, Integer>();
    }

    public void resetFruitsVeggies(Date date, int steps)
    {
        this.dailyFruitsVeggies.put(date, steps);
    }
}
