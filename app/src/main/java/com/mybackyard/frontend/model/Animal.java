package com.mybackyard.frontend.model;

import com.mybackyard.frontend.model.enums.AnimalSubType;
import com.mybackyard.frontend.model.enums.DietType;
import com.mybackyard.frontend.model.enums.NativeAreaType;

import java.util.List;

public class Animal {
    private long id;
    private String name;
    private AnimalSubType subType;
    private DietType dietType;
    private NativeAreaType nativeAreaType;
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

    public AnimalSubType getSubType() {
        return subType;
    }

    public void setSubType(AnimalSubType subType) {
        this.subType = subType;
    }

    public DietType getDietType() {
        return dietType;
    }

    public void setDietType(DietType dietType) {
        this.dietType = dietType;
    }

    public NativeAreaType getNativeAreaType() {
        return nativeAreaType;
    }

    public void setNativeAreaType(NativeAreaType nativeAreaType) {
        this.nativeAreaType = nativeAreaType;
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
