package com.list;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.list.model.User;
import com.list.repository.UserRepository;

@SpringBootApplication
public class ReactApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReactApplication.class, args);
    }
}

@RestController
@CrossOrigin(origins = "http://localhost:5173")
class FormController {

    @Autowired
    private UserRepository userRepository;

    private Map<String, User> tokenStore = new ConcurrentHashMap<>();
    private Map<String, String> uploadedFiles = new ConcurrentHashMap<>();

    @PostMapping("/api/register")
    public Map<String, String> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Map<String, String> response = new HashMap<>();
        if (userRepository.findUserByEmail(email).isPresent()) {
            response.put("success", "false");
            response.put("message", "Користувач з таким логіном вже існує. Придумайте інший.");
        } else {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            userRepository.save(user);

            response.put("success", "true");
            response.put("message", "Реєстрація успішна");
        }
        return response;
    }

    @PostMapping("/api/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Map<String, String> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findUserByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                String token = UUID.randomUUID().toString();
                tokenStore.put(token, user);

                response.put("success", "true");
                response.put("token", token);
                response.put("message", "Вхід успішний");
            } else {
                response.put("success", "false");
                response.put("message", "Невірний пароль");
            }
        } else {
            response.put("success", "false");
            response.put("message", "Користувач не знайдений");
        }
        return response;
    }

    @GetMapping("/api/validate")
    public Map<String, Boolean> validateToken(@RequestParam String token) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", tokenStore.containsKey(token));
        return response;
    }

    @PostMapping("/api/upload")
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        if (file.isEmpty()) {
            response.put("success", "false");
            response.put("message", "Файл порожній");
            return response;
        }

        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            File tempFile = new File(tempDir, file.getOriginalFilename());
            file.transferTo(tempFile);

            uploadedFiles.put(file.getOriginalFilename(), tempFile.getAbsolutePath());

            response.put("success", "true");
            response.put("message", "Файл завантажено успішно: " + tempFile.getAbsolutePath());
        } catch (IOException e) {
            response.put("success", "false");
            response.put("message", "Помилка при збереженні файлу: " + e.getMessage());
        }

        return response;
    }

    @GetMapping("/api/users/search")
    public List<Map<String, String>> searchUsers(@RequestParam String login) {
        return userRepository.findAll().stream()
                .filter(u -> u.getEmail().toLowerCase().contains(login.toLowerCase()))
                .map(u -> Map.of("id", u.getId().toString(), "login", u.getEmail()))
                .toList();
    }

    @GetMapping("/api/files/search")
    public List<Map<String, String>> searchFiles(@RequestParam String name) {
        return uploadedFiles.entrySet().stream()
                .filter(e -> e.getKey().toLowerCase().contains(name.toLowerCase()))
                .map(e -> Map.of("id", UUID.randomUUID().toString(), "filename", e.getKey()))
                .toList();
    }
}
