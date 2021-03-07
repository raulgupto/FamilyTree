package com.example;

import java.util.ArrayList;
import java.util.List;

class People{
    String name;
    People mother;
    String gender;
    List<People> sons = new ArrayList<>();
    List<People> daughters =  new ArrayList<>();
    People spouse;

    public People() {
    }

    public People(String name, People mother, String gender, People spouse) {
        this.name = name;
        this.mother = mother;
        this.gender = gender;
        this.spouse = spouse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public People getMother() {
        return mother;
    }

    public void setMother(People mother) {
        this.mother = mother;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<People> getSons() {
        return sons;
    }

    public void setSons(List<People> sons) {
        this.sons = sons;
    }

    public List<People> getDaughters() {
        return daughters;
    }

    public void setDaughters(List<People> daughters) {
        this.daughters = daughters;
    }

    public People getSpouse() {
        return spouse;
    }

    public void setSpouse(People spouse) {
        this.spouse = spouse;
    }
}
