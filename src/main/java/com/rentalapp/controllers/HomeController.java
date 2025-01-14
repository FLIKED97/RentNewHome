package com.rentalapp.controllers;

import com.rentalapp.models.Property;
import com.rentalapp.models.PropertyImage;
import com.rentalapp.services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final PropertyService propertyService;
    @GetMapping()
    public String homePage(Model model, @RequestParam(required = false) List<String> location,
                           @RequestParam(required = false) Integer roomCount,
                           @RequestParam(required = false) String priceSort,
                           @RequestParam(required = false) String areaSize) {
        Page<Property> properties = propertyService.getProperties(location, roomCount, priceSort, areaSize);

        model.addAttribute("properties", properties);
        model.addAttribute("location", location);
        model.addAttribute("roomCount", roomCount);
        model.addAttribute("priceSort", priceSort);
        model.addAttribute("areaSize", areaSize);
        model.addAttribute("properties", propertyService.getAllProperty());
        return "index";
    }
    @GetMapping("/error")
    public String errorPage(){
        return "errors/error404";
    }
}