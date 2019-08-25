package com.example.whereisthepotato;

import android.location.Location;

import java.time.LocalDate;
import java.util.Date;


public class Room {
    public int ID;
    public String name;
    public String type;
    public Date startTime;
    public Date endTime;
    public double rangeToSteal;
    public boolean notificationsEnabled;
    public double gameRange;
    public String region;
    public Location location;
    public User owner;
    public String password;
    public User[] players;
    public User[] potatoHolders;

    public Room() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Room(String name, String type, Date startTime, Date endTime, double rangeToSteal, boolean notificationsEnabled, double gameRange, String region, User owner, String password) {
        this.name = name;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rangeToSteal = rangeToSteal;
        this.notificationsEnabled = notificationsEnabled;
        this.gameRange = gameRange;
        this.region = region;
        this.owner = owner;
        this.password = password;
    }

    public String getName() {
        return name;
    }
}
