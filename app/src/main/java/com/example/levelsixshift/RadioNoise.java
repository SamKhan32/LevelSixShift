package com.example.levelsixshift;

import android.app.Application;

public class RadioNoise extends Application {
    //TODO: Rename this class
    private int money =0;
    private int level = 0;
    private int experience =0;
    private String avatar = "";

    public RadioNoise(){

    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
