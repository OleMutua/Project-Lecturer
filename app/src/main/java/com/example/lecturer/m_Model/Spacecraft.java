package com.example.lecturer.m_Model;

/**
 * Created by Musee on 2/11/2017.
 */

public class Spacecraft {    String code, name,propellant,description;
    public Spacecraft() {
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPropellant() {
        return propellant;
    }
    public void setPropellant(String propellant) {
        this.propellant = propellant;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}