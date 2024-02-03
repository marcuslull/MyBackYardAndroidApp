package com.mybackyard.frontend.model;

import com.mybackyard.frontend.model.enums.HardinessZone;
import com.mybackyard.frontend.model.enums.SoilType;
import com.mybackyard.frontend.model.enums.SunExposure;
import com.mybackyard.frontend.model.enums.YardSubType;

import java.util.List;

public class Yard {
    private long id;
    private String name;
    private HardinessZone hardinessZone;
    private SoilType soilType;
    private SunExposure sunExposure;
    private YardSubType yardSubType;
    private List<Long> noteIds;
    private List<Long> animalIds;
    private List<Long> plantIds;
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HardinessZone getHardinessZone() {
        return hardinessZone;
    }

    public void setHardinessZone(HardinessZone hardinessZone) {
        this.hardinessZone = hardinessZone;
    }

    public SoilType getSoilType() {
        return soilType;
    }

    public void setSoilType(SoilType soilType) {
        this.soilType = soilType;
    }

    public SunExposure getSunExposure() {
        return sunExposure;
    }

    public void setSunExposure(SunExposure sunExposure) {
        this.sunExposure = sunExposure;
    }

    public YardSubType getYardSubType() {
        return yardSubType;
    }

    public void setYardSubType(YardSubType yardSubType) {
        this.yardSubType = yardSubType;
    }

    public List<Long> getNoteIds() {
        return noteIds;
    }

    public void setNoteIds(List<Long> noteIds) {
        this.noteIds = noteIds;
    }

    public List<Long> getAnimalIds() {
        return animalIds;
    }

    public void setAnimalIds(List<Long> animalIds) {
        this.animalIds = animalIds;
    }

    public List<Long> getPlantIds() {
        return plantIds;
    }

    public void setPlantIds(List<Long> plantIds) {
        this.plantIds = plantIds;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
