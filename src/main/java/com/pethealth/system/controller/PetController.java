package com.pethealth.system.controller;

import com.pethealth.system.entity.Pet;
import com.pethealth.system.entity.User;
import com.pethealth.system.service.PetService;
import com.pethealth.system.service.UserService;
import com.pethealth.system.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    
    @Autowired
    private PetService petService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private Long getUserIdFromToken(String token) {
        String username = jwtUtil.extractUsername(token.substring(7));
        User user = userService.getProfile(username);
        return user.getId();
    }
    
    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestHeader("Authorization") String token, @RequestBody Pet pet) {
        Long userId = getUserIdFromToken(token);
        Pet createdPet = petService.createPet(pet, userId);
        return ResponseEntity.ok(createdPet);
    }
    
    @GetMapping
    public ResponseEntity<List<Pet>> getPets(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        List<Pet> pets = petService.getPetsByUserId(userId);
        return ResponseEntity.ok(pets);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Long userId = getUserIdFromToken(token);
        Pet pet = petService.getPetById(id, userId);
        return ResponseEntity.ok(pet);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Pet pet) {
        Long userId = getUserIdFromToken(token);
        Pet updatedPet = petService.updatePet(id, pet, userId);
        return ResponseEntity.ok(updatedPet);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Long userId = getUserIdFromToken(token);
        petService.deletePet(id, userId);
        return ResponseEntity.noContent().build();
    }
}
