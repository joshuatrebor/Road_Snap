package com.jsquad.snaproad.road_snap;

/**
 * Created by efc1980 on 7/22/2017.
 */

class User {

    public String firstName;
    public String lastName;
    public String userName;

    public User(String firstName, String lastName, String userName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

}
