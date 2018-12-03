package edu.umd.arturomcgill.healcity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User
{
    private String email;
    private String uid;
    private int totalSteps;
    private String profilePhotoEncoded;
    private ArrayList<String> lifetimeAchievements;
    private ArrayList<String> friendEmails;
    private ArrayList<String> upgradesPurchased;
    private ArrayList<String> goalsSet;
    private ArrayList<String> goalsMet;
    private ArrayList<Boolean> dailyGoals;
    private HashMap<String, Integer> dailyXP;
    private HashMap<String, Integer> dailyFruitsVeggies;
    private HashMap<String, Integer> dailySteps;
    private int level;
    private int lifetimeVolunteering;
    private int lifetimePublicTransportation;
    private int lifetimeParks;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int points;
    private int percentage;
    private double latitude;
    private double longitude;

    public User()
    {
        lifetimeAchievements = new ArrayList<String>();
        friendEmails = new ArrayList<String>();
        totalSteps = 0;
        profilePhotoEncoded = null;
        phoneNumber = null;
        upgradesPurchased = new ArrayList<String>();
        goalsMet = new ArrayList<String>();
        goalsSet = new ArrayList<String>();
        dailyGoals = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dailyGoals.add(false);
        }
        dailyXP = new HashMap<String, Integer>();
        dailySteps = new HashMap<String, Integer>();
        dailyFruitsVeggies = new HashMap<String, Integer>();
        firstName = "NoFirstName";
        lastName ="NoLastName";
        percentage = 0;
        latitude = 0.0;
        longitude = 0.0;
        level = 1;
        lifetimeParks = 0;
    }

    public User(String email, String uid, String firstName, String lastName)
    {
        this();
        this.email = email;
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dailyGoals = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dailyGoals.add(false);
        }
        percentage = 0;
        latitude = 0.0;
        longitude = 0.0;
        level = 1;
    }

    public User(String email, String uid)
    {
        this(email, uid, "NoFirstName", "NoLastName");
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public ArrayList<Boolean> getDailyGoals() {
        return dailyGoals;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public void finishGoal(String s) {
        if (s.equals("Adventuring Pedestrian")) {
            dailyGoals.set(0, true);
        } else if (s.equals("Pinch of Potassium")) {
            dailyGoals.set(1, true);
        } else if (s.equals("Mark the Park")) {
            dailyGoals.set(2, true);
        } else if (s.equals("Mass Transit Mass Savings")) {
            dailyGoals.set(3, true);
        } else { // Recycle
            dailyGoals.set(4, true);
        }
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

    public String getPhoneNumber() { return phoneNumber}

    public String setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
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

            for(Map.Entry<String, Integer> entry : dailySteps.entrySet())
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

            for(Map.Entry<String, Integer> entry : dailyFruitsVeggies.entrySet())
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


    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public void resetlifetimeParks()
    {
        this.lifetimeParks = 0;
    }

    public void addLifetimeParks(int parks)
    {
        this.lifetimeParks += parks;
    }

    public void setLifetimeParks(int parks)
    {
        this.lifetimeParks = parks;
    }

    public int getLifetimeParks()
    {
        return lifetimeParks;
    }

    public int getLifetimeXP()
    {
        if(dailyXP.size() == 0)
            return 0;
        else
        {
            int sum = 0;

            for(Map.Entry<String, Integer> entry : dailyXP.entrySet())
            {
                sum += entry.getValue();
            }

            return sum;
        }
    }

    public String getProfilePhotoEncoded()
    {
        return profilePhotoEncoded;
    }

    public void setProfilePhotoEncoded(String photoEncoded)
    {
        this.profilePhotoEncoded = photoEncoded;
    }

    public void setProfilePhoto(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageAsBytes = byteArrayOutputStream.toByteArray();

        setProfilePhotoEncoded(Base64.encodeToString(imageAsBytes, Base64.DEFAULT));
    }

    public Bitmap extractBitmap()
    {
        if(profilePhotoEncoded != null && !profilePhotoEncoded.equals("")) {
            byte[] imagesAsBytes = Base64.decode(profilePhotoEncoded.substring(profilePhotoEncoded.indexOf(",") + 1), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imagesAsBytes, 0, imagesAsBytes.length);
        }
        else
            return null;
    }

    public void resetProfilePhoto()
    {

        this.profilePhotoEncoded = null;
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

    public HashMap<String, Integer> getDailyXP()
    {
        return dailyXP;
    }

    public void setDailyXP(HashMap<String, Integer> dailyXP)
    {
        this.dailyXP = dailyXP;
    }

    public void resetDailyXP()
    {
        this.dailyXP = new HashMap<String, Integer>();
    }

    public void addDailyXPToDate(Date date, int xp)
    {
        this.dailyXP.put(date.toString(), xp);
    }

    public void addDailyXPToDate(String date, int xp)
    {
        this.dailyXP.put(date, xp);
    }

    public HashMap<String, Integer> getDailySteps()
    {
        return dailySteps;
    }

    public void setDailySteps(HashMap<String, Integer> dailySteps)
    {
        this.dailySteps = dailySteps;
    }

    public void resetDailySteps()
    {
        this.dailySteps = new HashMap<String, Integer>();
    }

    public void addDailystepsToDate(Date date, int steps)
    {
        this.dailySteps.put(date.toString(), steps);
    }

    public void addDailystepsToDate(String date, int steps)
    {
        this.dailySteps.put(date, steps);
    }

    public HashMap<String, Integer> getFruitsVeggies()
    {
        return dailyFruitsVeggies;
    }

    public void setFruitsVeggies(HashMap<String, Integer> dailyFruitsVeggies)
    {
        this.dailyFruitsVeggies = dailyFruitsVeggies;
    }

    public void resetFruitsVeggies()
    {
        this.dailyFruitsVeggies = new HashMap<String, Integer>();
    }

    public void setDailyFruitsVeggies(Date date, int steps)
    {
        this.dailyFruitsVeggies.put(date.toString(), steps);
    }

    public void setDailyFruitsVeggies(String date, int steps)
    {
        this.dailyFruitsVeggies.put(date, steps);
    }
}
