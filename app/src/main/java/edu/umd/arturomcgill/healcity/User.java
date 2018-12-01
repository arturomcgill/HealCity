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
    }

    public User(String email, String uid)
    {
        this();
        this.email = email;
        this.uid = uid;
    }

    public String getEmail()
    {
        return email;
    }


    public String getUid()
    {
        return uid;
    }

    public ArrayList<String> getLifetimeAchievements()
    {
        return lifetimeAchievements;
    }

    public void setLifetimeAchievements(ArrayList<String> lifetimeAchievements)
    {
        this.lifetimeAchievements = lifetimeAchievements;
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
}
