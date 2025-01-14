package com.rentalapp.services;

import com.rentalapp.models.Property;
import com.rentalapp.models.PropertyImage;
import com.rentalapp.models.User;
import com.rentalapp.models.UserRole;
import com.rentalapp.repositories.PropertyRepository;
import com.rentalapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public Property addProperty(Long userId, String title, String description, float price, String location,Integer roomCount,float area, String status) {
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
        property.setRoomCount(roomCount);
        property.setArea(area);
        property.setStatus(status);

        // Збереження оголошення в базі даних
        return propertyRepository.save(property);
    }

    public List<Property> getAllProperty() {
        return propertyRepository.findAll();
    }


    public Page<Property> getProperties(List<String> location, Integer roomCount, String priceSort, String areaSize) {
        Pageable pageable = Pageable.ofSize(10);
        Specification<Property> specification = Specification.where(null);

        // Фільтрація за локацією
        if (location != null && !location.isEmpty()) {
            specification = specification.and((root, query, builder) -> root.get("location").in(location));
        }

        // Фільтрація за кількістю кімнат
        if (roomCount != null) {
            specification = specification.and((root, query, builder) -> builder.equal(root.get("roomCount"), roomCount));
        }

        // Фільтрація за площею
        if (areaSize != null) {
            switch (areaSize) {
                case "small":
                    specification = specification.and((root, query, builder) -> builder.lessThan(root.get("area"), 50));
                    break;
                case "medium":
                    specification = specification.and((root, query, builder) -> builder.between(root.get("area"), 50, 100));
                    break;
                case "large":
                    specification = specification.and((root, query, builder) -> builder.greaterThan(root.get("area"), 100));
                    break;
            }
        }

        // Сортування за ціною
        if ("high-to-low".equals(priceSort)) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("price")));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.asc("price")));
        }

        return propertyRepository.findAll(specification, pageable);
    }
}