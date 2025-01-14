package com.rentalapp.services;


import com.rentalapp.Exception.UserAlreadyExistsException;
import com.rentalapp.models.User;
import com.rentalapp.models.UserRole;
import com.rentalapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistrationService {

    private final UserRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void register(User person){
        if (peopleRepository.findByEmail(person.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Користувач з email " + person.getEmail() + " вже існує");
        }
        person.setRating(0.0f);
        person.setRole(UserRole.TENANT);

        peopleRepository.save(person);
    }
}
