package com.rentalapp.services;

import com.rentalapp.models.PropertyImage;
import com.rentalapp.repositories.PropertyImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImagePropertyService {

    private final PropertyImageRepository propertyImageRepository;

    public ImagePropertyService(PropertyImageRepository propertyImageRepository) {
        this.propertyImageRepository = propertyImageRepository;
    }
    // Метод для збереження кількох фото
    public List<PropertyImage> saveImage(List<MultipartFile> files) throws IOException {
        List<PropertyImage> savedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            // Генерація унікального імені файлу
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get("/static/photo/property", fileName);

            // Створення директорії, якщо її немає
            Files.createDirectories(filePath.getParent());

            // Збереження файлу
            file.transferTo(filePath);

            // Створення об'єкта Photo і збереження його в БД
            PropertyImage image = new PropertyImage();
            image.setFileName(fileName);
            image.setImageUrl("/static/photo/property" + fileName);

            savedImages.add(propertyImageRepository.save(image));
        }

        return savedImages;
    }
    public void deleteImage(Long imageId) throws IOException {
        // Знаходимо фото за його ID
        PropertyImage image = propertyImageRepository.findById(imageId)
                .orElseThrow(() -> new FileNotFoundException("Photo not found with id " + imageId));

        // Видаляємо файл з файлової системи
        Path filePath = Paths.get("/static/photo/property", image.getFileName());
        Files.deleteIfExists(filePath);

        // Видаляємо запис з бази даних
        propertyImageRepository.delete(image);
    }

}
