package com.bazinga.model;

public class User {
    private Long id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;

    public User() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
