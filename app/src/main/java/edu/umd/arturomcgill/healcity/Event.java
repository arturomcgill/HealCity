package edu.umd.arturomcgill.healcity;

public class Event {
    private final String name;
    private final String description;
    private final String time;


    public Event(String name, String time, String description) {
        this.name = name;
        this.time = time;
        this.description = description;

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
}
