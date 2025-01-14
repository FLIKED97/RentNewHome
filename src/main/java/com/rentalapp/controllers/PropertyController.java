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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;

    @GetMapping("/add")
    public String getNewProperty(Model model){
        model.addAttribute("property", new Property());
        return "property/addProperty";
    }
    @PostMapping("/add")
    public String addProperty(@ModelAttribute("property") Property property){
        propertyService.addProperty(property);
        return "redirect:/home";
    }

}