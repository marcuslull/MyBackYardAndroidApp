package com.mybackyard.frontend.model;

import com.mybackyard.frontend.model.enums.HardinessZone;
import com.mybackyard.frontend.model.enums.NativeAreaType;
import com.mybackyard.frontend.model.enums.PlantSubType;
import com.mybackyard.frontend.model.enums.SoilType;
import com.mybackyard.frontend.model.enums.SunExposure;
import com.mybackyard.frontend.model.enums.WateringFrequency;

import java.util.List;

public class Plant {
    private long id;
    private String name;
    private HardinessZone hardinessZone;
    private NativeAreaType nativeAreaType;
    private PlantSubType plantSubType;
    private SoilType soilType;
    private SunExposure sunExposure;
    private WateringFrequency wateringFrequency;
    private List<Long> noteIds;
    private List<Long> imageIds;
    private long yardId;

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

    public NativeAreaType getNativeAreaType() {
        return nativeAreaType;
    }

    public void setNativeAreaType(NativeAreaType nativeAreaType) {
        this.nativeAreaType = nativeAreaType;
    }

    public PlantSubType getPlantSubType() {
        return plantSubType;
    }

    public void setPlantSubType(PlantSubType plantSubType) {
        this.plantSubType = plantSubType;
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

    public WateringFrequency getWateringFrequency() {
        return wateringFrequency;
    }

    public void setWateringFrequency(WateringFrequency wateringFrequency) {
        this.wateringFrequency = wateringFrequency;
    }

    public List<Long> getNoteIds() {
        return noteIds;
    }

    public void setNoteIds(List<Long> noteIds) {
        this.noteIds = noteIds;
    }

    public List<Long> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<Long> imageIds) {
        this.imageIds = imageIds;
    }

    public long getYardId() {
        return yardId;
    }

    public void setYardId(long yardId) {
        this.yardId = yardId;
    }
}
