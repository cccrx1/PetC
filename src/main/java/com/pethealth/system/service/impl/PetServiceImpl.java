package com.pethealth.system.service.impl;

import com.pethealth.system.entity.Pet;
import com.pethealth.system.entity.User;
import com.pethealth.system.repository.PetRepository;
import com.pethealth.system.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {
    
    @Autowired
    private PetRepository petRepository;
    
    @Override
    public Pet createPet(Pet pet, Long userId) {
        User user = new User();
        user.setId(userId);
        pet.setUser(user);
        return petRepository.save(pet);
    }
    
    @Override
    public List<Pet> getPetsByUserId(Long userId) {
        return petRepository.findByUserId(userId);
    }
    
    @Override
    public Pet getPetById(Long id, Long userId) {
        Optional<Pet> petOptional = petRepository.findById(id);
        if (petOptional.isPresent()) {
            Pet pet = petOptional.get();
            if (pet.getUser().getId().equals(userId)) {
                return pet;
            }
            throw new RuntimeException("Pet not found for this user");
        }
        throw new RuntimeException("Pet not found");
    }
    
    @Override
    public Pet updatePet(Long id, Pet pet, Long userId) {
        Pet existingPet = getPetById(id, userId);
        existingPet.setName(pet.getName());
        existingPet.setBreed(pet.getBreed());
        existingPet.setAge(pet.getAge());
        existingPet.setWeight(pet.getWeight());
        existingPet.setGender(pet.getGender());
        existingPet.setAvatar(pet.getAvatar());
        return petRepository.save(existingPet);
    }
    
    @Override
    public void deletePet(Long id, Long userId) {
        Pet pet = getPetById(id, userId);
        petRepository.delete(pet);
    }
}
