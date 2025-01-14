package com.rentalapp.controllers;



import com.rentalapp.Exception.UserAlreadyExistsException;
import com.rentalapp.models.User;
import com.rentalapp.services.RegistrationService;
import com.rentalapp.validators.UserValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final RegistrationService registrationService;
    private final UserValidator userValidator;

    @InitBinder("person")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @GetMapping("/login")
    public String loginPage(@ModelAttribute("person") User person) {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("person", new User());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("person") @Valid User person,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }

        try {
            registrationService.register(person);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Реєстрація успішна! Будь ласка, увійдіть в систему.");
            return "redirect:/auth/login";
        } catch (UserAlreadyExistsException e) {
            bindingResult.rejectValue("email", "error.user",
                    "Користувач з такою електронною поштою вже існує");
            return "auth/registration";
        }
    }
}
