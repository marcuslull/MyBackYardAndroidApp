package com.mybackyard.frontend.viewmodel;

import com.mybackyard.frontend.model.Animal;
import com.mybackyard.frontend.model.Image;
import com.mybackyard.frontend.model.Note;
import com.mybackyard.frontend.model.Plant;
import com.mybackyard.frontend.model.User;
import com.mybackyard.frontend.model.Yard;

import java.util.ArrayList;
import java.util.List;


public class MbyViewModelImpl implements MbyViewModel{

    private final List<Animal> animalData;
    private final List<Image> imageData;
    private final List<Note> noteData;
    private final List<Plant> plantData;
    private final List<User> userData;
    private final List<Yard> yardData;

    public MbyViewModelImpl() {
        this.animalData = new ArrayList<>();
        this.imageData = new ArrayList<>();
        this.noteData = new ArrayList<>();
        this.plantData = new ArrayList<>();
        this.userData = new ArrayList<>();
        this.yardData = new ArrayList<>();
    }

    @Override
    public List<Animal> getAnimalData() {
        return this.animalData;
    }

    @Override
    public void addAnimal(Animal animal) {
        this.animalData.add(animal);
    }

    @Override
    public void invalidateAnimal() {
        this.animalData.clear();
    }

    @Override
    public List<Image> getImageData() {
        return this.imageData;
    }

    @Override
    public void addImage(Image image) {
        this.imageData.add(image);
    }

    @Override
    public void invalidateImage() {
        this.imageData.clear();
    }

    @Override
    public List<Note> getNoteData() {
        return this.noteData;
    }

    @Override
    public void addNote(Note note) {
        this.noteData.add(note);
    }

    @Override
    public void invalidateNote() {
        this.noteData.clear();
    }

    @Override
    public List<Plant> getPlantData() {
        return this.plantData;
    }

    @Override
    public void addPlant(Plant plant) {
        this.plantData.add(plant);
    }

    @Override
    public void invalidatePlant() {
        this.plantData.clear();
    }

    @Override
    public List<User> getUserData() {
        return this.userData;
    }

    @Override
    public void addUser(User user) {
        this.userData.add(user);
    }

    @Override
    public void invalidateUser() {
        this.userData.clear();
    }

    @Override
    public List<Yard> getYardData() {
        return this.yardData;
    }

    @Override
    public void addYard(Yard yard) {
        this.yardData.add(yard);
    }

    @Override
    public void invalidateYard() {
        this.yardData.clear();
    }
}