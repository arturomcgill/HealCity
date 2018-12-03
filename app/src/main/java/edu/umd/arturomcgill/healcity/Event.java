package edu.umd.arturomcgill.healcity;

public class Event {
    private final String name;
    private final String description;
    private final String time;
    private final double latitude;
    private final double longitude;
    private final String address1;


    public Event(String name, String time, String description, double latitude, double longitude, String address1) {
        this.name = name;
        this.time = time;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address1 = address1;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() { 
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress1() {
        return address1;
    }

}
