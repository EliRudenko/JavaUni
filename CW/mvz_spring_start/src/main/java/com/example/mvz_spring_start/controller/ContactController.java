package com.example.mvz_spring_start.controller;

import com.example.mvz_spring_start.model.Contact;
import com.example.mvz_spring_start.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public String listContacts(Model model) {
        model.addAttribute("contacts", contactService.getAllContacts());
        return "contact-list";
    }

    @GetMapping("/add")
    public String addContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact-form";
    }

    @PostMapping("/add")
    public String addContact(@Valid @ModelAttribute Contact contact,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            return "contact-form";
        }
        try {
            contactService.saveContact(contact);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        return "redirect:/contacts";
    }

    @GetMapping("/edit/{id}")
    public String editContactForm(@PathVariable("id") Long id, Model model) {
        Optional<Contact> contact = contactService.getContactById(id);
        if (contact.isPresent()) {
            model.addAttribute("contact", contact.get());
            return "contact-form";
        } else {
            return "redirect:/contacts";
        }
    }

    @PostMapping("/edit/{id}")
    public String editContact(@PathVariable("id") Long id,
                              @Valid @ModelAttribute Contact contact,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "contact-form";
        }
        contact.setId(id);
        try {
            contactService.saveContact(contact);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
        return "redirect:/contacts";
    }

    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") Long id) {
        contactService.deleteContact(id);
        return "redirect:/contacts";
    }
}
