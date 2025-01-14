package com.rentalapp.controllers;

import com.rentalapp.models.Property;
import com.rentalapp.models.PropertyImage;
import com.rentalapp.services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final PropertyService propertyService;
    @GetMapping()
    public String homePage(Model model) {
        model.addAttribute("properties", propertyService.getAllProperty());
        return "index";
    }
    @GetMapping("/error")
    public String errorPage(){
        return "errors/error404";
    }
}