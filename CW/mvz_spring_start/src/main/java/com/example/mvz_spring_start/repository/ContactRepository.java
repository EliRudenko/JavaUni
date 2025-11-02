package com.example.mvz_spring_start.repository;

import com.example.mvz_spring_start.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByEmail(String email);
    Optional<Contact> findByPhone(String phone);
}
