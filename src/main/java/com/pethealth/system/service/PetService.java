package com.pethealth.system.service;

import com.pethealth.system.entity.Pet;
import java.util.List;

public interface PetService {
    Pet createPet(Pet pet, Long userId);
    List<Pet> getPetsByUserId(Long userId);
    Pet getPetById(Long id, Long userId);
    Pet updatePet(Long id, Pet pet, Long userId);
    void deletePet(Long id, Long userId);
}
