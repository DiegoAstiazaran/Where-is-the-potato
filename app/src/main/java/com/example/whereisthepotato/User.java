package com.example.whereisthepotato;

import android.location.Location;

public class User {

    public String email;
    public String name;
    public Location location;
    public String password;
    public Room[] rooms;

    public User(String email, String name, Location location, String password, Room[] rooms) {
        this.email = email;
        this.name = name;
        this.location = location;
        this.password = password;
        this.rooms = rooms;
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
