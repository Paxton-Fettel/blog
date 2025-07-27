package com.fettel.controller;

import com.fettel.model.User;
import com.fettel.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    //===========ROOT===========
    @GetMapping("")
    public String root() {
        return "redirect:/auth/login";
    }

    //===========LOGIN===========
    @GetMapping("auth/login")
    public String showLoginForm() {
        return "auth/login";
    }

    //===========REGISTER FORM===========
    @GetMapping("auth/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    //===========ADDING NEW USER===========
    @PostMapping("auth/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult bindingResult) {

        boolean usernameExists = userService.usernameExists(user.getUsername());
        boolean emailExists = userService.emailExists(user.getEmail());

        if(usernameExists) bindingResult.rejectValue("username", "error.user", "Username is already taken");
        if(emailExists) bindingResult.rejectValue("email", "error.user", "Email is already registered");
        if(bindingResult.hasErrors()) return "auth/register";

        userService.save(user);
        return "redirect:/auth/login";
    }
}
