package com.example.mvz_spring_start.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ім'я не може бути порожнім")
    private String name;

    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Некоректний формат email")
    private String email;

    @NotBlank(message = "Телефон не може бути порожнім")
    @Pattern(regexp = "\\+?\\d{10,13}", message = "Некоректний формат телефону (приклад: +380501234567)")
    private String phone;

    public Contact() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
