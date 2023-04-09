package com.level_zero.greeniq;

public class Profile extends AppCompat{
    private String userName;
    private String phoneNumber;
    private String profilePicture;
    private String location;
    private String email;
    private String password;
    private String id;
    private String coin;

    public Profile(String userName, String phoneNumber, String profilePicture, String location, String email, String password, String id, String coin) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.location = location;
        this.email = email;
        this.password = password;
        this.id = id;
        this.coin = coin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }
}
