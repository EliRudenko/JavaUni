package com.example.mvz_spring_start.service;

import com.example.mvz_spring_start.model.Contact;
import com.example.mvz_spring_start.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public Contact saveContact(Contact contact) {
        // перевірка унікальності email
        contactRepository.findByEmail(contact.getEmail()).ifPresent(existing -> {
            if (!existing.getId().equals(contact.getId())) {
                throw new RuntimeException("Контакт із таким email вже існує");
            }
        });

        // перевірка унікальності телефону
        contactRepository.findByPhone(contact.getPhone()).ifPresent(existing -> {
            if (!existing.getId().equals(contact.getId())) {
                throw new RuntimeException("Контакт із таким телефоном вже існує");
            }
        });

        return contactRepository.save(contact);
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}
