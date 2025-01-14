package com.rentalapp.controllers;

import com.rentalapp.models.Property;
import com.rentalapp.services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;

    @PostMapping("/add")
    public String addProperty(){
        return null;
    }

    @GetMapping()
    public String getProperties(@RequestParam(required = false) List<String> location,
                                @RequestParam(required = false) Integer roomCount,
                                @RequestParam(required = false) String priceSort,
                                @RequestParam(required = false) String areaSize,
                                Model model) {
        Page<Property> properties = propertyService.getProperties(location, roomCount, priceSort, areaSize);

        model.addAttribute("properties", properties);
        model.addAttribute("location", location);
        model.addAttribute("roomCount", roomCount);
        model.addAttribute("priceSort", priceSort);
        model.addAttribute("areaSize", areaSize);

        return "index";
    }

}