package security.example.spring_la_mia_pizzeria_security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import security.example.spring_la_mia_pizzeria_security.model.UserDto;
import security.example.spring_la_mia_pizzeria_security.repository.RoleRepository;
import security.example.spring_la_mia_pizzeria_security.security.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

   @Autowired
    private RoleRepository roleRepository;
   
    @GetMapping
    public String displayForm(Model model) {
        model.addAttribute("user", new UserDto());
        model.addAttribute("roles", roleRepository.findAll());
        return "register";
    }

    @PostMapping
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto, Model model, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return "register";  
            }
            userService.registerNewUserAccount(userDto);
            model.addAttribute("successMessage", "Registrazione completata!");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("roles", roleRepository.findAll());
            return "register";
        }
        return "redirect:/login";
    }
}


