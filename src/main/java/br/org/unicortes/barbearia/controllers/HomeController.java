package br.org.unicortes.barbearia.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/home")
public class HomeController {
    @GetMapping("/cliente")
    public String clientHome(){
        return "Cliente Home";
    }

    @GetMapping("/admin")
    public String adminHome(){
        return "Admin Home";
    }

    @GetMapping("/barbeiro")
    public String barberHome(){
        return "Barber Home";
    }
}