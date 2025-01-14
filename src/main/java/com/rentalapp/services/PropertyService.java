package com.rentalapp.services;

import com.rentalapp.models.Property;
import com.rentalapp.models.PropertyImage;
import com.rentalapp.models.User;
import com.rentalapp.models.UserRole;
import com.rentalapp.repositories.PropertyRepository;
import com.rentalapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public Property addProperty(Long userId, String title, String description, float price, String location, String status) {
        // Перевірка чи користувач має роль "LANDLORD"
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != UserRole.ROLE_LANDLORD) {
            throw new RuntimeException("Only Landlords can add properties");
        }

        // Створення нового оголошення
        Property property = new Property();
        property.setLandlord(user);
        property.setTitle(title);
        property.setDescription(description);
        property.setPrice(price);
        property.setLocation(location);
        property.setStatus(status);

        // Збереження оголошення в базі даних
        return propertyRepository.save(property);
    }

    public List<Property> getAllProperty() {
        return propertyRepository.findAll();
    }
}