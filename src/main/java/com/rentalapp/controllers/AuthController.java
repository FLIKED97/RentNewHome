package com.rentalapp.controllers;



import com.rentalapp.models.User;
import com.rentalapp.services.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;

    @GetMapping("/login")
    public String loginPage(@ModelAttribute("person") User person){
        return "auth/login";
    }
        @GetMapping("/registration")
        public String showRegistrationForm(Model model) {
            model.addAttribute("person", new User());  // Create empty Person object for form binding
            return "auth/registration";  // Return the registration.html template name
        }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("person") @Valid User person,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        registrationService.register(person);
        return "redirect:/auth/login";
    }


}
