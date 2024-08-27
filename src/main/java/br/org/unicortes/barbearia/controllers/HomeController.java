package br.org.unicortes.barbearia.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/")
public class HomeController {

    @GetMapping("/cliente/home")
    public String clientHome(){
        return "Cliente Home";
    }

    @GetMapping("/admin/home")
    public String adminHome(){
        return "Admin Home";
    }

    @GetMapping("/barbeiro/home")
    public String barberHome(){
        return "Barber Home";
    }
}
