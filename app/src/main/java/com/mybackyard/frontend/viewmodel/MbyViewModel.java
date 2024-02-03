package com.mybackyard.frontend.viewmodel;

import com.mybackyard.frontend.model.Animal;
import com.mybackyard.frontend.model.Image;
import com.mybackyard.frontend.model.Note;
import com.mybackyard.frontend.model.Plant;
import com.mybackyard.frontend.model.User;
import com.mybackyard.frontend.model.Yard;

import java.util.List;

public interface MbyViewModel {
    List<Animal> getAnimalData();

    void addAnimal(Animal animal);

    void invalidateAnimal();

    List<Image> getImageData();

    void addImage(Image image);

    void invalidateImage();

    List<Note> getNoteData();

    void addNote(Note note);

    void invalidateNote();

    List<Plant> getPlantData();

    void addPlant(Plant plant);

    void invalidatePlant();

    List<User> getUserData();

    void addUser(User user);

    void invalidateUser();

    List<Yard> getYardData();

    void addYard(Yard yard);

    void invalidateYard();
}
